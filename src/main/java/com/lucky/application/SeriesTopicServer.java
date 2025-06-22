package com.lucky.application;

import cn.hutool.core.collection.CollectionUtil;
import com.lucky.domain.GradeService;
import com.lucky.domain.PrizeInfoService;
import com.lucky.domain.SeriesTopicService;
import com.lucky.domain.SessionInfoService;
import com.lucky.domain.entity.GradeEntity;
import com.lucky.domain.entity.SeriesTopicEntity;
import com.lucky.domain.entity.SessionInfoEntity;
import com.lucky.domain.exception.BusinessException;
import com.lucky.domain.valueobject.Inventory;
import com.lucky.domain.valueobject.SeriesTopicDetail;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 主题 系列
 */
@Component
public class SeriesTopicServer {
    private final SeriesTopicService seriesTopicService;
    private final PrizeInfoService prizeInfoService;
    private final SessionInfoService sessionInfoService;
    private final GradeService gradeService;

    public SeriesTopicServer(SeriesTopicService seriesTopicService,
                             PrizeInfoService prizeInfoService,
                             SessionInfoService sessionInfoService, GradeService gradeService) {
        this.seriesTopicService = seriesTopicService;
        this.prizeInfoService = prizeInfoService;
        this.sessionInfoService = sessionInfoService;
        this.gradeService = gradeService;
    }

    /**
     * 添加/修改
     */
    public Long saveOrUpdate(SeriesTopicEntity entity) {
        return seriesTopicService.saveOrUpdate(entity);
    }

    /**
     * 修改状态
     */
    public void updateStatus(Long id, Boolean status) {
        var seriesTopicEntity = SeriesTopicEntity.builder()
                .id(id)
                .status(status)
                .build();
        seriesTopicService.saveOrUpdate(seriesTopicEntity);
    }

    /**
     * 删除
     */
    public Boolean deleteById(Long id) {
        return seriesTopicService.deleteById(id);
    }

    /**
     * 列表
     */
    public List<SeriesTopicDetail> findByList() {
        List<SeriesTopicEntity> byList = seriesTopicService.findByList();
        if (CollectionUtils.isEmpty(byList))
            return List.of();

        var gradeIds = byList.stream()
                .map(SeriesTopicEntity::getGradeIds)
                .filter(CollectionUtil::isNotEmpty)
                .flatMap(List::stream)
                .collect(Collectors.toList());

        List<GradeEntity> byIds = gradeService.findByIds(gradeIds);
        var gradeMapName = byIds.stream()
                .collect(Collectors.toMap(GradeEntity::getId, GradeEntity::getName));
        return byList.stream()
                .map(entity -> SeriesTopicDetail.getInstance(entity, gradeMapName))
                .collect(Collectors.toList());

    }

    /**
     * 设置场次
     */
    @Transactional(rollbackFor = Exception.class)
    public Boolean setFieldsNumber(Long topicId, Integer number) {
        var seriesTopicEntity = seriesTopicService.findById(topicId);

        seriesTopicEntity.setSession(number);
        seriesTopicService.saveOrUpdate(seriesTopicEntity);


        //查询奖品
        var prizeInfoEntityList = prizeInfoService.findByTopicId(topicId);

        if (CollectionUtils.isEmpty(prizeInfoEntityList))
            throw BusinessException.newInstance("缺少奖品，请先设置奖品");

        prizeInfoEntityList = prizeInfoEntityList.stream()
                .filter(s -> Objects.equals(s.getType(), 2))
                .collect(Collectors.toList());

        if (CollectionUtils.isEmpty(prizeInfoEntityList))
            throw BusinessException.newInstance("缺少普通级别的奖品，请先设置奖品");

        //场次的产品和库存
        var inventoryList = prizeInfoEntityList.stream()
                .map(Inventory::getInstance)
                .collect(Collectors.toList());

        var sessionInfoEntity = SessionInfoEntity.builder()
                .prizeInventory(inventoryList)
                .topicId(topicId)
                .status(1)
                .build();
        //添加场次

        var aBoolean = sessionInfoService.addSession(sessionInfoEntity, number, topicId);
        if (aBoolean)
            return true;
        throw BusinessException.newInstance("添加场次失败");
    }

    public SeriesTopicDetail findById(Long id) {
        SeriesTopicEntity byId = seriesTopicService.findById(id);
        if (Objects.isNull(byId))
            return null;
        var gradeIds = byId.getGradeIds();
        if (CollectionUtils.isEmpty(gradeIds))
            return SeriesTopicDetail.getInstance(byId, null);
        var gradeEntityList = gradeService.findByIds(gradeIds);
        var gradeMapName = gradeEntityList.stream()
                .collect(Collectors.toMap(GradeEntity::getId, GradeEntity::getName));
        return SeriesTopicDetail.getInstance(byId, gradeMapName);
    }

}
