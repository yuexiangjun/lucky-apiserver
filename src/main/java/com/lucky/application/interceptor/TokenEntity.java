package com.lucky.application.interceptor;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TokenEntity {

	/**
	 * 用户id
	 */
	private String userId;
	/**
	 * 用户名称
	 */
	private String username;
	/**
	 * 用户登录的客户端
	 * 1：后台 2：小程序
	 */
	private Integer client;
	/**
	 * 创建时间
	 */
	private String createTime;


}
