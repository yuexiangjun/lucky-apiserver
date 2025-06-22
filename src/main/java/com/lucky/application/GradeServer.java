package com.lucky.application;

import com.lucky.domain.GradeService;
import com.lucky.domain.entity.GradeEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GradeServer {
	private final GradeService gradeService;

	public GradeServer(GradeService gradeService) {
		this.gradeService = gradeService;
	}

	/**
	 * 添加/修修改奖项
	 */
	public Long saveOrUpdate(GradeEntity entity) {
		return gradeService.saveOrUpdate(entity);
	}

	/**
	 * 启用禁用
	 */
	public void enabled(Long id, Boolean enabled) {
		var entity = GradeEntity.builder()
				.status(enabled)
				.id(id)
				.build();
		gradeService.saveOrUpdate(entity);
	}

	/**
	 * 列表
	 */
	public List<GradeEntity> findByList(GradeEntity entity) {
		return gradeService.findByList(entity);
	}

	/**
	 * 删除
	 */
	public Boolean deleteById(Long id) {
		return gradeService.deleteById(id);
	}
}
