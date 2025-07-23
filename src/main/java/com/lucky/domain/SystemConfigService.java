package com.lucky.domain;

import com.lucky.domain.entity.SystemConfigEntity;
import com.lucky.domain.repository.SystemConfigRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SystemConfigService {
	private final SystemConfigRepository systemConfigRepository;

	public SystemConfigService(SystemConfigRepository systemConfigRepository) {
		this.systemConfigRepository = systemConfigRepository;
	}

	/**
	 * 添加修改
	 */
	public void saveOrUpdate(SystemConfigEntity entity) {
		systemConfigRepository.saveOrUpdate(entity);
	}

	/**
	 * 更据奖项id查询
	 */
	public SystemConfigEntity findByGradeId(Long id) {
		return systemConfigRepository.findByGradeId(id);
	}


	/**
	 * 更据奖项ids查询
	 */
	public List<SystemConfigEntity> findByGradeIds(List<Long> ids) {
		return systemConfigRepository.findByGradeIds(ids);
	}

	/**
	 * 列表
	 */
	public List<SystemConfigEntity> findAll() {
		return systemConfigRepository.findAll();
	}

}
