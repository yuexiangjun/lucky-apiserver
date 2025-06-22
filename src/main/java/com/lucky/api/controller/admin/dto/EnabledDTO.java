package com.lucky.api.controller.admin.dto;

import lombok.Data;

@Data
public class EnabledDTO {
	/**
	 * 数据id
	 */
	private Long id;
	/**
	 * 是否启用
	 */
	private Boolean enabled;
}
