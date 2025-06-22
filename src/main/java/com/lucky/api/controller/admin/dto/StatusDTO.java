package com.lucky.api.controller.admin.dto;

import lombok.Data;

@Data
public class StatusDTO {
	/**
	 * id
	 */
	private Long id;
	/**
	 * 状态    1-待收货 2-完成
	 */
	private Integer status;
}
