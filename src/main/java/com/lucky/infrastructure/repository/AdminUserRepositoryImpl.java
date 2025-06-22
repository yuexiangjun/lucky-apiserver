package com.lucky.infrastructure.repository;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lucky.domain.entity.AdminUserEntity;
import com.lucky.domain.repository.AdminUserRepository;
import com.lucky.infrastructure.repository.mysql.mapper.AdminUserMapper;
import com.lucky.infrastructure.repository.mysql.po.AdminUserPO;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class AdminUserRepositoryImpl extends ServiceImpl<AdminUserMapper, AdminUserPO> implements AdminUserRepository {

	@Override
	public AdminUserEntity getByPhone(String phone) {

		var wrapper = Wrappers.lambdaQuery(AdminUserPO.class)
				.eq(AdminUserPO::getPhone, phone);

		var one = this.getOne(wrapper);

		return AdminUserPO.toEntity(one);
	}

	@Override
	public Long saveOrUpdate(AdminUserEntity entity) {
		var po = AdminUserPO.getInstance(entity);

		if (Objects.isNull(po))
			return null;

		this.saveOrUpdate(po);

		return po.getId();
	}

	@Override
	public List<AdminUserEntity> list(AdminUserEntity entity) {
		var wrapper = Wrappers.lambdaQuery(AdminUserPO.class)
				.eq(Strings.isNotBlank(entity.getPhone()), AdminUserPO::getPhone, entity.getPhone())
				.eq(Strings.isNotBlank(entity.getName()), AdminUserPO::getName, entity.getName());

		return this.list(wrapper)
				.stream()
				.map(AdminUserPO::toEntity)
				.collect(Collectors.toList());
	}

	@Override
	public AdminUserEntity getById(Long id) {
		var wrapper = Wrappers.lambdaQuery(AdminUserPO.class)
				.eq(AdminUserPO::getId, id);

		var one = this.getOne(wrapper);

		return AdminUserPO.toEntity(one);
	}
}
