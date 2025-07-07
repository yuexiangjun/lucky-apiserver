package com.lucky.api.controller.external;

import com.alibaba.fastjson.JSONObject;
import com.lucky.api.controller.common.BaseController;
import com.lucky.api.controller.external.dto.LogisticsOrderDTO;
import com.lucky.api.controller.external.dto.PayDTO;
import com.lucky.api.controller.external.vo.SuccessProductsVO;
import com.lucky.api.utils.ResponseFormat;
import com.lucky.application.LotteryServer;
import com.lucky.domain.valueobject.PayInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 支付
 *
 * @folder API/小程序/支付相关
 */
@RequestMapping("/api/wechat/pay")
@RestController
@Slf4j
public class LotteryController extends BaseController {
	private final LotteryServer lotteryServer;

	public LotteryController(LotteryServer lotteryServer) {
		this.lotteryServer = lotteryServer;
	}

	/**
	 * 获取准备上面的排队人数
	 */
	@GetMapping("/buy/queue/num")
	@ResponseFormat
	public Integer getBuyQueueNum(@RequestParam("topicId") Long topicId,
	                              @RequestParam("sessionId") Long sessionId) {

		return lotteryServer.getQueueNum(topicId, sessionId);
	}

	/**
	 * 购买同意场次 排队
	 */
	@GetMapping("/buy")
	@ResponseFormat
	public Boolean buy(@RequestParam("topicId") Long topicId,
	                   @RequestParam("sessionId") Long sessionId,
	                   @RequestParam(value = "wechatUserId", required = false) Long wechatUserId) {
		if (Objects.isNull(wechatUserId))
			wechatUserId = this.getWechatUserId();
		return lotteryServer.buy(topicId, sessionId, wechatUserId);
	}


	/**
	 * 抽奖结束
	 */
	@GetMapping("/buy/end")
	@ResponseFormat
	public void end(@RequestParam("topicId") Long topicId, @RequestParam("sessionId") Long sessionId) {
		lotteryServer.end(topicId, sessionId);
	}

	/**
	 * 获取控场时间
	 */
	@GetMapping("/buy/control-time")
	@ResponseFormat
	public Integer getControlTime(@RequestParam("topicId") Long topicId,
	                              @RequestParam("sessionId") Long sessionId) {
		return lotteryServer.getControlTime(topicId, sessionId);
	}

	/**
	 * 微信支付
	 */
	@PostMapping("/tripartite-pay")
	@ResponseFormat
	public PayInfo pay(@RequestBody PayDTO dto) {

		if (Objects.isNull(dto.getWechatUserId()))
			dto.setWechatUserId(this.getWechatUserId());
//            dto.setWechatUserId(1937732128810778625l);

		return lotteryServer.pay(PayDTO.toTripartiteEntity(dto));
	}


	/**
	 * 支付成功后 获取抽取的奖品
	 */
	@GetMapping("/pay/success")
	@ResponseFormat
	public List<SuccessProductsVO> successByPrizeInfo(@RequestParam("payOrderId") Long payOrderId) {
		return lotteryServer.successByPrizeInfo(payOrderId)
				.stream()
				.map(SuccessProductsVO::getInstance)
				.collect(Collectors.toList());

	}



	/**
	 * 账户积分支付
	 */
	@PostMapping("/balance-pay")
	@ResponseFormat
	public List<SuccessProductsVO> balancePay(@RequestBody PayDTO dto) {

		if (Objects.isNull(dto.getWechatUserId()))
			dto.setWechatUserId(this.getWechatUserId());

		return lotteryServer.balancePay(PayDTO.toBalanceEntity(dto))
				.stream()
				.map(SuccessProductsVO::getInstance)
				.collect(Collectors.toList());
	}

	/**
	 * 支付回调
	 *
	 * @param jsonObject
	 * @return
	 */
	@RequestMapping("/callback")
	public String payCallBack(@RequestBody JSONObject jsonObject) {
		log.info("支付回调:{}", jsonObject);
		return lotteryServer.payCallBack(jsonObject);
	}



	/**
	 *生成  需要支付的物流费的 物流定单
	 */
	@PostMapping("/generate-logistics-order-pay")
	@ResponseFormat
	public PayInfo generateLogisticsOrderPay(@RequestBody LogisticsOrderDTO dto) {


		if (Objects.isNull(dto.getWechatUserId()))
			dto.setWechatUserId(this.getWechatUserId());

		var logisticsOrder = LogisticsOrderDTO.toLogisticsOrder(dto);
		return lotteryServer.generateLogisticsOrderPay(logisticsOrder);
	}

}
