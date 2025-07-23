package com.lucky.infrastructure.repository;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lucky.domain.entity.SystemConfigEntity;
import com.lucky.domain.repository.SystemConfigRepository;
import com.lucky.infrastructure.repository.mysql.mapper.SystemConfigMapper;
import com.lucky.infrastructure.repository.mysql.po.SystemConfigPO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class SystemConfigRepositoryImpl extends ServiceImpl<SystemConfigMapper, SystemConfigPO> implements SystemConfigRepository {
	private final SystemConfigMapper systemConfigMapper;


	public SystemConfigRepositoryImpl(SystemConfigMapper systemConfigMapper) {
		this.systemConfigMapper = systemConfigMapper;
	}


	@Override
	public void saveOrUpdate(SystemConfigEntity entity) {

		var po = SystemConfigPO.getInstance(entity);
		this.saveOrUpdate(po);
	}

	@Override
	public SystemConfigEntity findByGradeId(Long gradeId) {

		var eq = Wrappers.lambdaQuery(SystemConfigPO.class)
				.eq(SystemConfigPO::getGradeId, gradeId);
		var po = this.getOne(eq);


		return SystemConfigPO.toEntity(po);
	}

	@Override
	public List<SystemConfigEntity> findByGradeIds(List<Long> ids) {

		if (CollectionUtils.isEmpty(ids))
			return List.of();

		var wrapper = Wrappers.lambdaQuery(SystemConfigPO.class)
				.in(SystemConfigPO::getGradeId, ids);

		return this.list(wrapper)
				.stream()
				.map(SystemConfigPO::toEntity)
				.collect(Collectors.toList());

	}

	@Override
	public List<SystemConfigEntity> findAll() {
		var wrapper = Wrappers.lambdaQuery(SystemConfigPO.class);
		return this.list(wrapper)
				.stream()
				.map(SystemConfigPO::toEntity)
				.collect(Collectors.toList());

	}

	@Override
	public void deleteById(Long id) {

		this.removeById(id);
	}
}
