package com.lucky.domain.repository;

import com.lucky.domain.entity.AdminUserEntity;

import java.util.List;

public  interface AdminUserRepository {
	AdminUserEntity getByPhone(String phone);

	 Long saveOrUpdate(AdminUserEntity adminUserEntity);

	List<AdminUserEntity> list(AdminUserEntity adminUserEntity);

	AdminUserEntity getById(Long id);
}
