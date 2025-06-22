package com.lucky.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 奖项
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GradeEntity {
	/**
	 * id
	 */
	private Long id;

	/**
	 * 类别
	 * 1:隐藏
	 * 2：普通级别
	 */
	private Integer type;
	/**
	 * 排序
	 */
	private Integer sort;
	/**
	 * 名称
	 */
	private String name;
	/**
	 * 概率
	 */
	private BigDecimal probability;
	/**
	 * 是否启用
	 */
	private Boolean status;

}
