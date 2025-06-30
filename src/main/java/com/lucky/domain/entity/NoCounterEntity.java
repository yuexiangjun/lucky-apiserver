package com.lucky.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * 公共表 - CRM-编号计数器
 *
 * @TableName no_counter
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NoCounterEntity {
	/**
	 * id
	 */

	private Long id;

	/**
	 * 编号类型
	 */

	private String type;

	/**
	 * 数值
	 */

	private Integer value;

	/**
	 * 所属天
	 */

	private LocalDate belongDay;

}