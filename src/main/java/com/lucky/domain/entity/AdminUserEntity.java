package com.lucky.domain.entity;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminUserEntity {
	/**
	 * id
	 */
	private Long id;
	/**
	 * 姓名
	 */
	private String name;
	/**
	 * 手机号码
	 */
	private String phone;
	/**
	 * 密码
	 */
	private String password;
	/**
	 * 是否启用
	 */
	private Boolean enabled;
	/**
	 * 最后一次登录时间
	 */
	private LocalDateTime lastLoginTime;
	/**
	 * 创建时间
	 */
	private LocalDateTime createTime;

}
