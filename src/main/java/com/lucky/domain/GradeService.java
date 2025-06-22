package com.lucky.domain;

import com.lucky.domain.entity.GradeEntity;
import com.lucky.domain.repository.GradeRepository;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 奖项的管理
 */
@Component
public class GradeService {
    private final GradeRepository gradeRepository;

    public GradeService(GradeRepository gradeRepository) {
        this.gradeRepository = gradeRepository;
    }

    /**
     * 添加/修修改奖项
     */
    public Long saveOrUpdate(GradeEntity entity) {
        return gradeRepository.saveOrUpdate(entity);
    }
    /**
     * 列表
     */
    public List<GradeEntity> findByList(GradeEntity entity){
          return gradeRepository.findByList(entity);
    }
    /**
     *删除
     */
    public Boolean deleteById(Long id){
        return gradeRepository.deleteById(id);
    }

    public  List<GradeEntity> findByIds(List<Long> gradeIds) {
        return gradeRepository.findByIds(gradeIds);
    }
}
