package com.lucky.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class BalanceLogEntity {
	/**
	 * id
	 */
	private Long id;
	/**
	 * 微信用户id
	 */
	private Long wechatUserId;
	/**
	 * 金额
	 */
	private BigDecimal money;
	/**
	 * 操作人id
	 */
	private Long operatorId;
	/**
	 * 操作时间
	 */
	private LocalDateTime operateTime;
}
