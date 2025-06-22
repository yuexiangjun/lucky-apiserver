package com.lucky.domain;

import com.lucky.domain.entity.AdminUserEntity;
import com.lucky.domain.repository.AdminUserRepository;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component

public class AdminUserService {
	private final AdminUserRepository adminUserRepository;


	public AdminUserService(AdminUserRepository adminUserRepository) {
		this.adminUserRepository = adminUserRepository;
	}

	/**
	 * 更据手机号码获取用户
	 */
	public AdminUserEntity getByPhone(String phone) {
		if (Strings.isBlank(phone))
			return null;
		return adminUserRepository.getByPhone(phone);
	}

	/**
	 * 添加/修改用户
	 */
	public Long saveOrUpdate(AdminUserEntity adminUserEntity) {
		return adminUserRepository.saveOrUpdate(adminUserEntity);
	}

	/**
	 * 列表
	 */
	public List<AdminUserEntity> list(AdminUserEntity adminUserEntity) {
		return adminUserRepository.list(adminUserEntity);
	}

	/**
	 * 更据id查询
	 */
	public AdminUserEntity getById(Long id) {
		if (Objects.isNull(id))
			return null;
		return adminUserRepository.getById(id);
	}


}
