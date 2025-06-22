package com.lucky.api.controller.admin.vo;

import cn.hutool.core.bean.BeanUtil;
import com.lucky.domain.entity.AdminUserEntity;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminUserVO {
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
	public static AdminUserVO getInstance(AdminUserEntity entity){
		if (Objects.isNull(entity))
			return null;
		return BeanUtil.toBean(entity,AdminUserVO.class);
	}

}