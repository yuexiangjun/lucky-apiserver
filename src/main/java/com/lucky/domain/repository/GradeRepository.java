package com.lucky.domain.repository;

import com.lucky.domain.entity.GradeEntity;

import java.util.List;

public interface GradeRepository {
    /**
     * 添加修改
     * @param entity
     * @return
     */
    Long saveOrUpdate(GradeEntity entity);

    /**
     * 查询集合
     * @return
     */
    List<GradeEntity> findByList(GradeEntity entity);

    /**
     * 删除
     * @param id
     * @return
     */
    Boolean deleteById(Long id);

    List<GradeEntity> findByIds(List<Long> gradeIds);
}
