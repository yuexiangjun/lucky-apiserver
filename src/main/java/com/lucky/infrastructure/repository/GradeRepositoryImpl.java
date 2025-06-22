package com.lucky.infrastructure.repository;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lucky.domain.entity.GradeEntity;
import com.lucky.domain.repository.GradeRepository;
import com.lucky.infrastructure.repository.mysql.mapper.GradeMapper;
import com.lucky.infrastructure.repository.mysql.po.GradePO;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class GradeRepositoryImpl extends ServiceImpl<GradeMapper, GradePO> implements GradeRepository {
    private final GradeMapper gradeMapper;

    public GradeRepositoryImpl(GradeMapper gradeMapper) {
        this.gradeMapper = gradeMapper;

    }

    @Override
    public Long saveOrUpdate(GradeEntity entity) {

        var gradePO = GradePO.getInstance(entity);

        if (Objects.isNull(gradePO))
            return null;

        if (Objects.isNull(gradePO.getId()))
            gradeMapper.insert(gradePO);
        else
            gradeMapper.updateById(gradePO);
        return gradePO.getId();
    }

    @Override
    public List<GradeEntity> findByList(GradeEntity entity) {
        var wrapper = Wrappers.<GradePO>lambdaQuery()
                .eq(Objects.nonNull(entity.getStatus()), GradePO::getStatus, entity.getStatus())
                .eq(Objects.nonNull(entity.getType()), GradePO::getType, entity.getType())
                .orderByAsc(GradePO::getType)
                .orderByAsc(GradePO::getSort);

        return gradeMapper.selectList(wrapper)
                .stream()
                .map(GradePO::toEntity)
                .collect(Collectors.toList());

    }

    @Override
    public Boolean deleteById(Long id) {
        int i = gradeMapper.deleteById(id);
        if (i > 0)
            return true;
        else
            return false;

    }

    @Override
    public List<GradeEntity> findByIds(List<Long> gradeIds) {

        if (CollectionUtils.isEmpty(gradeIds))
            return List.of();

        var wrapper = Wrappers.<GradePO>lambdaQuery()
                .in(GradePO::getId, gradeIds);

        return gradeMapper.selectList(wrapper)
                .stream()
                .map(GradePO::toEntity)
                .collect(Collectors.toList());

    }
}
