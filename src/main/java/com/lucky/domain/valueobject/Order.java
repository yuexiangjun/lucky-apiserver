package com.lucky.domain.valueobject;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {
	/**
	 * 订单id
	 */
	private Long id;
	/**
	 * 微信用户
	 */
	private String wechatName;
	/**
	 * 主题
	 */
	private String topicName;

	/**
	 * 商品id
	 */
	private String productName;
	/**
	 * 商品图片
	 */
	private String productUrl;
	/**
	 * 奖项名称
	 */
	private String sessionName;
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



}