package com.lucky.domain.entity;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class WechatUserEntity {
	/**
	 * id
	 */
	private Long id;
	/**
	 * 头像地址
	 */
	private String avatar;
	/**
	 * openid
	 */
	private String openid;
	/**
	 * 姓名
	 */
	private String name;
	/**
	 * 手机号码
	 */
	private String phone;
	/**
	 * 账户余额
	 */
	private BigDecimal balance;

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
