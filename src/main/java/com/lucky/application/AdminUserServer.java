package com.lucky.application;


import com.lucky.application.interceptor.JwtUtils;
import com.lucky.application.interceptor.LoginUserEntity;
import com.lucky.application.interceptor.TokenEntity;
import com.lucky.domain.AdminUserService;
import com.lucky.domain.entity.AdminUserEntity;
import com.lucky.domain.exception.BusinessException;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Component
public class AdminUserServer {
	private final AdminUserService adminUserService;

	public AdminUserServer(AdminUserService adminUserService) {
		this.adminUserService = adminUserService;
	}

	/**
	 * 添加/修改
	 *
	 * @param entity
	 * @return
	 */
	public Long saveOrUpdate(AdminUserEntity entity) {
		return adminUserService.saveOrUpdate(entity);
	}

	/**
	 * 列表
	 *
	 * @param entity
	 * @return
	 */
	public List<AdminUserEntity> list(AdminUserEntity entity) {
		return adminUserService.list(entity);
	}

	/**
	 * 启用禁用
	 */
	public void enabled(Long id, Boolean enabled) {
		var entity = AdminUserEntity.builder()
				.enabled(enabled)
				.id(id)
				.build();
		adminUserService.saveOrUpdate(entity);
	}

	/**
	 * 登录
	 */
	public LoginUserEntity login(String phone, String password) {
		if (phone == null || password == null) {
			throw BusinessException.newInstance("手机号或密码不能为空");
		}
		var entity = adminUserService.getByPhone(phone);
		if (entity == null) {
			throw BusinessException.newInstance("手机号不存在");
		}
		if (!Objects.equals(entity.getPassword(), password)) {
			throw BusinessException.newInstance("密码错误");
		}
		if (!entity.getEnabled()) {
			throw BusinessException.newInstance("账号已被禁用");
		}
		var tokenEntity = TokenEntity.builder()
				.userId(String.valueOf(entity.getId()))
				.username(entity.getName())
				.client(1)
				.createTime(String.valueOf(System.currentTimeMillis()))
				.build();

		var token = JwtUtils.createToken(tokenEntity);
		//修改最后一次登录时间
		entity.setLastLoginTime(LocalDateTime.now());

		adminUserService.saveOrUpdate(entity);

		return LoginUserEntity.builder()
				.authorization(token)
				.username(entity.getName())
				.phone(entity.getPhone())
				.id(entity.getId())
				.build();
	}
}
