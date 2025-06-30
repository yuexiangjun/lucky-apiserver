package com.lucky.api.controller.admin.dto;

import lombok.Data;

@Data
public class WechatUserFindListDTO {
	/**
	 * 姓名
	 */
	private String name;
	/**
	 * 手机号码
	 */
	private String phone;
	/**
	 * 负责人
	 */
	private String  ownerName;
}
