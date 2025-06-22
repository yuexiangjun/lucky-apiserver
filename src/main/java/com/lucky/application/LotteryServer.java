package com.lucky.application;

import com.alibaba.fastjson.JSONObject;
import com.lucky.application.tripartite.WeChatPayServer;
import com.lucky.domain.PayOrderService;
import com.lucky.domain.RedisService;
import com.lucky.domain.SeriesTopicService;
import com.lucky.domain.WechatUserService;
import com.lucky.domain.config.RedissionConfig;
import com.lucky.domain.entity.PayOrderEntity;
import com.lucky.domain.exception.BusinessException;
import com.lucky.domain.valueobject.PayInfo;
import com.lucky.domain.valueobject.PayOrderPram;
import com.lucky.domain.valueobject.SuccessProducts;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Component
public class LotteryServer {
    private final PayOrderService payOrderService;
    private final OrderServer orderServer;
    private final SeriesTopicService topicService;
    private final RedissionConfig redissionConfig;
    private final WeChatPayServer weChatPayServer;
    private final WechatUserService wechatUserService;
    private final RedisService redisService;

    private final static String PAY_LOCK_NAME = "PAY_LOCK_NAME:";
    private final static String LOCATION = "LOCATION:";

    public LotteryServer(PayOrderService payOrderService, OrderServer orderServer,
                         SeriesTopicService topicService,
                         RedissionConfig redissionConfig, WeChatPayServer weChatPayServer, WechatUserService wechatUserService, RedisService redisService) {
        this.payOrderService = payOrderService;
        this.orderServer = orderServer;

        this.topicService = topicService;
        this.redissionConfig = redissionConfig;
        this.weChatPayServer = weChatPayServer;
        this.wechatUserService = wechatUserService;
        this.redisService = redisService;
    }

    /**
     * 获取准备上面的排队人数
     */
    public Integer getQueueNum(Long topicId, Long sessionId) {

        this.verifySession(sessionId);

        var key = getByBuyKey(topicId, sessionId);
        return redisService.getCacheObject(key) == null ? 0 : 1;
    }

    private void verifySession(Long sessionId) {
        var sessionInfoEntity = orderServer.getSessionInfoService().findById(sessionId);

        if (Objects.equals(sessionInfoEntity.getStatus(), 2))
            throw BusinessException.newInstance("本场已结束，请选择下一场");
    }

    /**
     * 购买同意场次 排队
     */
    public Boolean buy(Long topicId, Long sessionId, Long wechatUserId) {

        this.verifySession(sessionId);

        var key = this.getByBuyKey(topicId, sessionId);

        var cacheObject = redisService.getCacheObject(key);
        if (Objects.nonNull(cacheObject) && !Objects.equals(cacheObject, String.valueOf(wechatUserId))) {
            return false;
        }
        //去掉上一场排队信息 设置当前人排队信息
        this.putLocation(topicId, sessionId, wechatUserId);
        //设置当前场次
        this.putRedisKey(key, String.valueOf(wechatUserId));
        return true;
    }

    /**
     * 去掉上一场的排队信息
     * 设置新得排队信息
     */
    private void putLocation(Long topicId, Long sessionId, Long wechatUserId) {
        var key = LOCATION.concat(wechatUserId.toString());


        var cacheObject = redisService.getCacheObject(key);
        var oldValue = String.valueOf(cacheObject);
        var split = oldValue.split("-");

        if (split.length == 2) {
            var topicId1 = Long.valueOf(split[0]);
            var sessionId1 = Long.valueOf(split[1]);

            this.end(topicId1, sessionId1);
        }

        var value = String.valueOf(topicId).concat("-").concat(String.valueOf(sessionId));
        redisService.setCacheObject(key, value);
    }


    /**
     * 抽奖结束
     */
    public void end(Long topicId, Long sessionId) {

        var key = this.getByBuyKey(topicId, sessionId);

        redisService.deleteObject(key);
    }


    /**
     * 支付
     */
    @Transactional(rollbackFor = Exception.class)
    public PayInfo pay(PayOrderEntity entity) {

        this.verifySession(entity.getSessionId());

        var key = getRedisKey(entity);
        //获取是否本场有人在操作
        String cacheObject = redisService.getCacheObject(key);

        if (Objects.nonNull(cacheObject) && !Objects.equals(cacheObject, String.valueOf(entity.getWechatUserId()))) {
            throw BusinessException.newInstance("本场有人在操作,请稍等...");
        }

        var lock = redissionConfig.redissonClient().getLock(key);
        lock.lock();
        try {
            //获取支付金额
            var seriesTopic = topicService.findById(entity.getTopicId());

            var totalMoney = seriesTopic.getPrice().multiply(BigDecimal.valueOf(entity.getTimes()));

            entity.setPayMoney(totalMoney);
            entity.setPayStatus(0);
            entity.setPayTime(LocalDateTime.now());

            var payOrderId = payOrderService.saveOrUpdate(entity);

            var wechatUserEntity = wechatUserService.getById(entity.getWechatUserId());
            //调取三方支付接口
            var payOrderPram = PayOrderPram.getInstance(payOrderId, totalMoney, wechatUserEntity.getOpenid());
            var pay = weChatPayServer.pay(payOrderPram);

            entity.setId(payOrderId);
            entity.setPayParams(JSONObject.toJSONString(pay.getPayParams()));

            payOrderService.saveOrUpdate(entity);

            return PayInfo.builder()
                    .payOrderId(payOrderId)
                    .payParams(pay.getPayParams())
                    .build();

        } finally {
            if (lock.isLocked())
                lock.unlock();
        }
    }

    private void putRedisKey(String PAY_LOCK_NAME, String entity) {
        redisService.setCacheObject(PAY_LOCK_NAME, entity, 180, TimeUnit.SECONDS);
    }

    private static String getRedisKey(PayOrderEntity entity) {
        var suffix = List.of(entity.getTopicId(), entity.getSessionId())
                .stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));
        return PAY_LOCK_NAME.concat(suffix);

    }


    /**
     * 支付成功 抽取的奖品
     */
    @Transactional(rollbackFor = Exception.class)
    public List<SuccessProducts> successByPrizeInfo(Long payOrderId) {

        var payOrder = payOrderService.getById(payOrderId);
        if (Objects.equals(payOrder.getPayStatus(), 0)) {
            var payOrderReturnValue = weChatPayServer.payQueryOrder(payOrderId);
            payOrder.setPayStatus(Objects.equals(payOrderReturnValue.getPayResult(), "SUCCESS") ? 1 : 2);
            payOrder.setThirdPayId(payOrderReturnValue.getThirdPayId());
            payOrder.setCompleteTime(LocalDateTime.now());
            //延长控场
            if (Objects.equals(payOrder.getPayStatus(), 1))
                this.delayBuy(payOrder.getTopicId(), payOrder.getSessionId(), payOrder.getWechatUserId());
        }

        var payOrderEntity = orderServer.getByPrizeInfo(payOrder);

        payOrderService.saveOrUpdate(payOrderEntity);

        return orderServer.getPrizeInfoByNum(payOrderEntity.getPrizeId());


    }


    /**
     * 支付回调
     */
    @Transactional(rollbackFor = Exception.class)
    public String payCallBack(JSONObject jsonObject) {


        var payOrderReturnValue = weChatPayServer.payCallBack(jsonObject);
        var payOrder = payOrderService.getById(payOrderReturnValue.getPayOrderId());
        if (Objects.isNull(payOrder))
            return "FAIL";
        payOrder.setPayStatus(Objects.equals(payOrderReturnValue.getPayResult(), "SUCCESS") ? 1 : 2);
        payOrder.setThirdPayId(payOrderReturnValue.getThirdPayId());
        payOrder.setCompleteTime(LocalDateTime.now());
        payOrderService.saveOrUpdate(payOrder);
        //延长控场
        this.delayBuy(payOrder.getTopicId(), payOrder.getSessionId(), payOrder.getWechatUserId());
        return "SUCCESS";
    }

    private void delayBuy(Long topicId, Long sessionId, Long wechatUserId) {
        var key = getByBuyKey(topicId, sessionId);
        putRedisKey(key, String.valueOf(wechatUserId));
    }

    private static String getByBuyKey(Long topicId, Long sessionId) {
        var key = PAY_LOCK_NAME.concat("buy:")
                .concat(String.valueOf(topicId))
                .concat(":")
                .concat(String.valueOf(sessionId));
        return key;
    }

    @Transactional(rollbackFor = Exception.class)
    public List<SuccessProducts> balancePay(PayOrderEntity entity) {

        this.verifySession(entity.getSessionId());

        var key = getRedisKey(entity);
        //获取是否本场有人在操作
        String cacheObject = redisService.getCacheObject(key);

        if (Objects.nonNull(cacheObject) && !Objects.equals(cacheObject, String.valueOf(entity.getWechatUserId()))) {
            throw BusinessException.newInstance("本场有人在操作,请稍等...");
        }

        var lock = redissionConfig.redissonClient().getLock(key);
        lock.lock();
        try {
            //获取支付金额
            var seriesTopic = topicService.findById(entity.getTopicId());

            var totalMoney = seriesTopic.getPrice().multiply(BigDecimal.valueOf(entity.getTimes()));

            entity.setPayMoney(totalMoney);
            entity.setPayStatus(0);
            entity.setPayTime(LocalDateTime.now());

            var payOrderId = payOrderService.saveOrUpdate(entity);

            entity.setId(payOrderId);

            //TODO：账户余额得扣除
            wechatUserService.balanceReduce(entity.getWechatUserId(), totalMoney, "福袋抽奖");

            //抽取奖品
            var payOrderEntity = orderServer.getByPrizeInfo(entity);
            payOrderEntity.setPayStatus(1);
            payOrderService.saveOrUpdate(payOrderEntity);

            //延长控场
            this.delayBuy(payOrderEntity.getTopicId(), payOrderEntity.getSessionId(), payOrderEntity.getWechatUserId());

            return orderServer.getPrizeInfoByNum(payOrderEntity.getPrizeId());


        } finally {
            if (lock.isLocked())
                lock.unlock();
        }


    }

    public Integer getControlTime(Long topicId, Long sessionId) {
        long expire = redisService.getExpire(getByBuyKey(topicId, sessionId));

        return expire > 0 ? (int) expire : 0;
    }
}
