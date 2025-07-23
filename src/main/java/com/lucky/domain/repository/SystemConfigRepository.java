package com.lucky.domain.repository;

import com.lucky.domain.entity.SystemConfigEntity;

import java.util.List;

public interface SystemConfigRepository {

	/**
	 * 添加修改
	 */
	void saveOrUpdate(SystemConfigEntity entity);

	/**
	 * 更据奖项id查询
	 */
	SystemConfigEntity findByGradeId(Long id);

	/**
	 * 更据奖项ids查询
	 */
	List<SystemConfigEntity> findByGradeIds(List<Long> ids);

	/**
	 * 列表
	 */
	List<SystemConfigEntity> findAll();
}
