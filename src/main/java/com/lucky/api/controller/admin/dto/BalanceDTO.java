package com.lucky.api.controller.admin.dto;

import lombok.Data;

import java.math.BigDecimal;
@Data
public class BalanceDTO {
	/**
	 * 用户id
	 */
	private Long wechatUserId;
	/**
	 * 金额
	 */
	private BigDecimal money;
}
