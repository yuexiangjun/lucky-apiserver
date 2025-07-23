package com.lucky.domain.entity;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class SystemConfigEntity {
	/**
	 * 系统配置id
	 */

	private Long id;
	/**
	 * 奖品等级id
	 */
	private Long gradeId;
	/**
	 * 最低消费金额
	 */
	private BigDecimal minConsume;
}
