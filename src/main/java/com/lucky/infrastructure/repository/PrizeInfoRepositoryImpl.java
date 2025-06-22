package com.lucky.infrastructure.repository;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lucky.domain.entity.PrizeInfoEntity;
import com.lucky.domain.exception.BusinessException;
import com.lucky.domain.repository.PrizeInfoRepository;
import com.lucky.infrastructure.repository.mysql.mapper.PrizeInfoMapper;
import com.lucky.infrastructure.repository.mysql.po.PrizeInfoPO;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 奖品
 */
@Component
public class PrizeInfoRepositoryImpl extends ServiceImpl<PrizeInfoMapper, PrizeInfoPO> implements PrizeInfoRepository {
    private final PrizeInfoMapper prizeInfoMapper;

    public PrizeInfoRepositoryImpl(PrizeInfoMapper prizeInfoMapper) {
        this.prizeInfoMapper = prizeInfoMapper;
    }


    /**
     * 添加/修改
     */
    public Long saveOrUpdate(PrizeInfoEntity entity) {

        var gradePO = PrizeInfoPO.getInstance(entity);

        if (Objects.isNull(gradePO))
            return null;

        if (Objects.isNull(gradePO.getId()))
            prizeInfoMapper.insert(gradePO);
        else
            prizeInfoMapper.updateById(gradePO);
        return gradePO.getId();
    }

    /**
     * 删除
     */

    public Boolean deleteById(Long id) {
        int i = prizeInfoMapper.deleteById(id);
        if (i > 0)
            return true;
        else
            return false;
    }

    @Override
    public List<PrizeInfoEntity> findByTopicId(Long topicId) {
        var wrapper = Wrappers.lambdaQuery(PrizeInfoPO.class)
                .eq(PrizeInfoPO::getTopicId, topicId);
        return prizeInfoMapper.selectList(wrapper)
                .stream()
                .map(PrizeInfoPO::toEntity)
                .collect(Collectors.toList());

    }

    @Override
    public List<PrizeInfoEntity> findByIds(List<Long> productIds) {
        if (CollectionUtils.isEmpty(productIds))
            return List.of();

        var wrapper = Wrappers.lambdaQuery(PrizeInfoPO.class)
                .in(PrizeInfoPO::getId, productIds);
        return prizeInfoMapper.selectList(wrapper)
                .stream()
                .map(PrizeInfoPO::toEntity)
                .collect(Collectors.toList());

    }

    @Override
    public Boolean saveOrUpdateList(List<PrizeInfoEntity> entity, Long topicId) {


        if (Objects.isNull(topicId))
            throw BusinessException.newInstance("缺少TopicId");

        var wrapper = Wrappers.lambdaQuery(PrizeInfoPO.class)
                .eq(PrizeInfoPO::getTopicId, topicId);

        this.remove(wrapper);

        if (!CollectionUtils.isEmpty(entity)) {
            var prizeInfopos = entity.stream()
                    .map(PrizeInfoPO::getInstance)
                    .collect(Collectors.toList());

            return this.saveOrUpdateBatch(prizeInfopos);
        }
        return true;
    }

    @Override
    public List<PrizeInfoEntity> findByGradeIds(List<Long> gradeIds) {
        if (CollectionUtils.isEmpty(gradeIds))
            return List.of();

        var wrapper = Wrappers.lambdaQuery(PrizeInfoPO.class)
                .in(PrizeInfoPO::getGradeId, gradeIds);
        return prizeInfoMapper.selectList(wrapper)
                .stream()
                .map(PrizeInfoPO::toEntity)
                .collect(Collectors.toList());
    }
}
