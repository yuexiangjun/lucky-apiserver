package com.lucky.api.controller.admin.dto;

import lombok.Data;

@Data
public class LoginDTO {
	/**
	 * 手机号码
	 */
	private String phone;
	/**
	 * 密码
	 */
	private String password;
}
