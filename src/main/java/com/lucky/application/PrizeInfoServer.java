package com.lucky.application;

import com.lucky.domain.GradeService;
import com.lucky.domain.PrizeInfoService;
import com.lucky.domain.entity.GradeEntity;
import com.lucky.domain.entity.PrizeInfoEntity;
import com.lucky.domain.exception.BusinessException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 奖品
 */
@Component
public class PrizeInfoServer {
    private final PrizeInfoService prizeInfoService;
    private final GradeService gradeService;

    public PrizeInfoServer(PrizeInfoService prizeInfoService, GradeService gradeService) {
        this.prizeInfoService = prizeInfoService;
        this.gradeService = gradeService;
    }


    /**
     * 添加/修改
     */
    @Transactional(rollbackFor = Exception.class)
    public Long saveOrUpdate(PrizeInfoEntity entity) {
        return prizeInfoService.saveOrUpdate(entity);
    }

    /**
     * 批量添加/修改
     */
    @Transactional(rollbackFor = Exception.class)
    public Boolean saveOrUpdateList(List<PrizeInfoEntity> entity, Long topicId) {

        if (Objects.isNull(topicId))
            throw BusinessException.newInstance("奖品所属系列为空");
        if (!CollectionUtils.isEmpty(entity)) {
            var gradeIds = entity.stream()
                    .map(PrizeInfoEntity::getGradeId)
                    .collect(Collectors.toList());

            var gradeMapName = gradeService.findByIds(gradeIds).stream()
                    .collect(Collectors.toMap(GradeEntity::getId, GradeEntity::getType, (a, b) -> a));

            entity = entity
                    .stream()
                    .peek(s -> s.setType(gradeMapName.get(s.getGradeId())))
                    .collect(Collectors.toList());
        }

        return prizeInfoService.saveOrUpdateList(entity, topicId);
    }

    /**
     * 删除
     */
    public Boolean deleteById(Long id) {
        return prizeInfoService.deleteById(id);
    }

    public List<PrizeInfoEntity> findByTopicId(Long topicId) {
        var prizeInfoEntityList = prizeInfoService.findByTopicId(topicId);

        if (CollectionUtils.isEmpty(prizeInfoEntityList))
            return List.of();

        var gradeIds = prizeInfoEntityList.stream()
                .map(PrizeInfoEntity::getGradeId)
                .collect(Collectors.toList());

        var gradeMapName = gradeService.findByIds(gradeIds).stream()
                .collect(Collectors.toMap(GradeEntity::getId, GradeEntity::getType, (a, b) -> a));

        return prizeInfoEntityList
                .stream()
                .peek(s -> s.setType(gradeMapName.get(s.getGradeId())))
                .collect(Collectors.toList());
    }
}
