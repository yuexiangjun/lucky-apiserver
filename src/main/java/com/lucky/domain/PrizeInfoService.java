package com.lucky.domain;

import com.lucky.domain.entity.PrizeInfoEntity;
import com.lucky.domain.repository.PrizeInfoRepository;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 奖品
 */
@Component
public class PrizeInfoService {
    private final PrizeInfoRepository prizeInfoRepository;

    public PrizeInfoService(PrizeInfoRepository prizeInfoRepository) {
        this.prizeInfoRepository = prizeInfoRepository;
    }


    /**
     * 添加/修改
     */
    public Long saveOrUpdate(PrizeInfoEntity entity) {
        return prizeInfoRepository.saveOrUpdate(entity);
    }

    /**
     * 批量添加/修改
     */
    public  Boolean saveOrUpdateList(List<PrizeInfoEntity> entity, Long topicId) {
        return prizeInfoRepository.saveOrUpdateList(entity,topicId);
    }

    /**
     * 删除
     */
    public Boolean deleteById(Long id) {
        return prizeInfoRepository.deleteById(id);
    }

    public List<PrizeInfoEntity> findByTopicId(Long topicId) {
        return  prizeInfoRepository .findByTopicId(topicId);

    }

    public List<PrizeInfoEntity> findByIds(List<Long> productIds) {
        return  prizeInfoRepository .findByIds(productIds);
    }

    public List<PrizeInfoEntity> findByGradeIds(List<Long> gradeIds) {
        return  prizeInfoRepository .findByGradeIds(gradeIds);
    }
}
