package com.lucky.application;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.lucky.domain.LogisticsOrderService;
import com.lucky.domain.PrizeInfoService;
import com.lucky.domain.WechatUserService;
import com.lucky.domain.entity.*;
import com.lucky.domain.valueobject.LogisticsOrderInfo;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class LogisticsOrderServer {
    private final LogisticsOrderService logisticsOrderService;
    private final PrizeInfoService prizeInfoService;
    private final WechatUserService wechatUserService;

    public LogisticsOrderServer(LogisticsOrderService logisticsOrderService, PrizeInfoService prizeInfoService, WechatUserService wechatUserService) {
        this.logisticsOrderService = logisticsOrderService;
        this.prizeInfoService = prizeInfoService;
        this.wechatUserService = wechatUserService;
    }


    /**
     * 查询物流订单
     */
    public List<LogisticsOrderInfo> getByWechatUserId(Long wechatUserId) {

        var logisticsOrderEntities = logisticsOrderService.getByWechatUserId(wechatUserId);

        return this.getLogisticsOrderInfos(logisticsOrderEntities);


    }

    private List<LogisticsOrderInfo> getLogisticsOrderInfos(List<LogisticsOrderEntity> logisticsOrderEntities) {

        if (CollectionUtil.isEmpty(logisticsOrderEntities))
            return List.of();

        var productIds = logisticsOrderEntities.stream()
                .map(s -> s.getGoods()
                        .stream()
                        .map(LogisticsOrderPrizeEntity::getProductId)
                        .collect(Collectors.toList())
                )
                .filter(CollectionUtil::isNotEmpty)
                .flatMap(List::stream)
                .collect(Collectors.toSet());

        var productIdList = productIds.stream().distinct().collect(Collectors.toList());

        var prizeInfoMap = prizeInfoService.findByIds(productIdList)
                .stream()
                .collect(Collectors.toMap(PrizeInfoEntity::getId, Function.identity()));

        //获取用户的手机号码

        var wechatUserIds = logisticsOrderEntities.stream()
                .map(LogisticsOrderEntity::getWechatUserId)
                .collect(Collectors.toList());

        var wechatUserMap = wechatUserService.getByIds(wechatUserIds)
                .stream()
                .collect(Collectors.toMap(WechatUserEntity::getId, Function.identity()));




		return logisticsOrderEntities
				.stream()
				.map(s -> {
					var prizeInfoEntities = s.getGoods()
							.stream()
							.collect(Collectors.groupingBy(LogisticsOrderPrizeEntity::getProductId))
							.entrySet()
							.stream()
							.map(s1 -> {

								var prizeInfoEntity = prizeInfoMap.get(s1.getKey());

								 var bean = BeanUtil.toBean(prizeInfoEntity, PrizeInfoEntity.class);

								List<LogisticsOrderPrizeEntity> value = s1.getValue();
								bean.setInventory(CollectionUtil.isEmpty(value) ? 0 : value.size());
								return bean;
							}).collect(Collectors.toList());

					return LogisticsOrderInfo.builder()
							.id(s.getId())
							.status(s.getStatus())
							.number(s.getNumber())
							.logisticsNumber(s.getLogisticsNumber())
							.logisticsCompany(s.getLogisticsCompany())
							.createTime(s.getCreateTime())
							.sendTime(s.getSendTime())
							.completeTime(s.getCompleteTime())
							.goods(prizeInfoEntities)
                            .name(DeliveryAddressEntity.getNameStr(s.getAddress()))
							.address(DeliveryAddressEntity.getAddressStr(s.getAddress()))
							.phone(wechatUserMap.getOrDefault(s.getWechatUserId(), new WechatUserEntity()).getPhone())
							.build();
				}).collect(Collectors.toList());
    }

    /**
     * 后台查询物流订单
     */
    public List<LogisticsOrderInfo> getByAdminList(LogisticsOrderEntity entity, String phone) {


        if (Strings.isNotBlank(phone)) {
            var wechatUserEntity = wechatUserService.getByPhone(phone);
            if (!Objects.isNull(wechatUserEntity))
                entity.setWechatUserId(wechatUserEntity.getId());
        }


        var logisticsOrderEntities = logisticsOrderService.getByAdminList(entity);


        return this.getLogisticsOrderInfos(logisticsOrderEntities);
    }


    /**
     * 修改物流订单
     */
    public void updateLogisticsOrder(LogisticsOrderEntity logisticsOrderEntity) {
        if (Objects.nonNull(logisticsOrderEntity.getStatus())) {
            if (logisticsOrderEntity.getStatus() == 1) {
                logisticsOrderEntity.setSendTime(LocalDateTime.now());
            }
            if (logisticsOrderEntity.getStatus() == 2) {
                logisticsOrderEntity.setCompleteTime(LocalDateTime.now());
            }
        }

        logisticsOrderService.updateLogisticsOrder(logisticsOrderEntity);
    }


}
