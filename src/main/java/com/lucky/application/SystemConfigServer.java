package com.lucky.application;

import com.lucky.domain.GradeService;
import com.lucky.domain.SystemConfigService;
import com.lucky.domain.entity.GradeEntity;
import com.lucky.domain.entity.SystemConfigEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class SystemConfigServer {
	private final SystemConfigService systemConfigService;

	private final GradeService gradeService;


	public SystemConfigServer(SystemConfigService systemConfigService, GradeService gradeService) {
		this.systemConfigService = systemConfigService;
		this.gradeService = gradeService;
	}

	/**
	 * 添加修改
	 */
	public void saveOrUpdate(SystemConfigEntity entity) {
		systemConfigService.saveOrUpdate(entity);
	}

	/**
	 * 更据奖项id查询
	 */
	public SystemConfigEntity findByGradeId(Long id) {
		return systemConfigService.findByGradeId(id);
	}


	/**
	 * 更据奖项ids查询
	 */
	public List<SystemConfigEntity> findByGradeIds(List<Long> ids) {
		return systemConfigService.findByGradeIds(ids);
	}

	/**
	 * 列表
	 */
	public List<SystemConfigEntity> findAll() {
		var systemConfigEntities = systemConfigService.findAll();

		if (CollectionUtils.isEmpty(systemConfigEntities))
			return List.of();

		var gradeIds = systemConfigEntities.stream()
				.map(SystemConfigEntity::getGradeId)
				.collect(Collectors.toList());

		var gradeEntityMap = gradeService.findByIds(gradeIds)
				.stream()
				.collect(Collectors.toMap(GradeEntity::getId, Function.identity()));
		return systemConfigEntities.stream()
				.map(entity -> {
					var gradeEntity = gradeEntityMap.getOrDefault(entity.getGradeId(), new GradeEntity());
					entity.setGradeName(gradeEntity.getName());
					return entity;
				})
				.collect(Collectors.toList());

	}

	/**
	 * 删除
	 */
	public void deleteById(Long id) {
		systemConfigService.deleteById(id);
	}



	public List<GradeEntity> findByList() {
		return gradeService.findByList(new GradeEntity());
	}
}
