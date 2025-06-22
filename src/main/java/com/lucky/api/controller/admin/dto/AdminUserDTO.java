package com.lucky.api.controller.admin.dto;

import cn.hutool.core.bean.BeanUtil;
import com.lucky.domain.entity.AdminUserEntity;
import com.lucky.domain.exception.BusinessException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class AdminUserDTO {
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


	public static AdminUserEntity toEntity(AdminUserDTO dto) {
		if (Objects.isNull(dto))
			throw BusinessException.newInstance("参数缺失");

		var bean = BeanUtil.toBean(dto, AdminUserEntity.class);
		if (Objects.isNull(dto.getId()))
			bean.setCreateTime(LocalDateTime.now());
		return bean;


	}

}