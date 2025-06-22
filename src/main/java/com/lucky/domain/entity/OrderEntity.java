package com.lucky.domain.entity;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderEntity {
	/**
	 * 订单id
	 */
	private Long id;
	/**
	 * 微信用户id
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
	 * 支付订单id
	 */
	private Long payOrderId;

	/**
	 * 订单状态  0-待发货 1-待收货 2-完成
	 */
	private Integer status;
	/**
	 * 订单创建时间
	 */
	private LocalDateTime createTime;
	/**
	 * 完成时间
	 */
	private LocalDateTime finishTime;
	/**
	 * 发货时间
	 */
	private LocalDateTime sendTime;
	/**
	 * 商品
	 */
	private List<OrderPrizeEntity> orderPrizeEntities;





}
