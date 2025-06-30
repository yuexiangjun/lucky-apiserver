package com.lucky.domain.enumes;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum NoCounterType {
	/**
	 * 物流单号
	 */
	Logistics_order("物流单号",  "WL"),
	;

	/**
	 * 解释
	 */
	private String desc;


	/**
	 * 前缀
	 */
	private String prefix;
}
