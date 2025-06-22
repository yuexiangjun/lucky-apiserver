package com.lucky.application.interceptor;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class LoginUserEntity {
	/**
	 * 用户id
	 */
	private Long id;
	/**
	 * 用户名
	 */
	private String username;
	/**
	 * Authorization
	 */
	private String authorization;
	/**
	 * 头像地址
	 */
	private String avatar;
	/**
	 * 手机号码
	 */
	private String phone;
}
