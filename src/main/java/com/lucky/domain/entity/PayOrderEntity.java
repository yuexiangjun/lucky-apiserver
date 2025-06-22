package com.lucky.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class PayOrderEntity {
	/**
	 * id
	 */
	private Long id;

	/**
	 * 支付金额
	 */
	private BigDecimal payMoney;
	/**
	 * 支付类型 1 微信支付 2：平台积分支付
	 */
	private Integer payType;
	/**
	 * 订单类型 1 抽奖订单 2：充值订单
	 */
	private Integer orderType;

	/**
	 * 支付状态 0 未支付 1 已支付 2：支付失败
	 */
	private Integer payStatus;
	/**
	 * 支付人
	 */
	private Long wechatUserId;
	/**
	 * 主题id
	 */
	private Long topicId;
	/**
	 * 场次id
	 */
	private Long sessionId;
	/**
	 * 抽奖次数
	 */
	private Integer times;
	/**
	 * 支付时间
	 */
	private LocalDateTime payTime;
	/**
	 * 总金额
	 */
	private BigDecimal totalMoney;
	/**
	 * 完成时间
	 */
	private LocalDateTime completeTime;
	/**
	 * 三方支付id
	 */
	private String thirdPayId;
	/**
	 * 商品id
	 */

	private List<Long > prizeId;
	/**
	 * 支付前端需要的参数
	 */
	private String payParams;

}
