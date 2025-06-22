package com.lucky.domain;

import com.lucky.domain.entity.SeriesTopicEntity;
import com.lucky.domain.repository.SeriesTopicRepository;
import org.springframework.stereotype.Component;

import java.util.List;
/**
 * 主题 系列
 */
@Component
public class SeriesTopicService {
    private final SeriesTopicRepository seriesTopicRepository;

    public SeriesTopicService(SeriesTopicRepository seriesTopicRepository) {
        this.seriesTopicRepository = seriesTopicRepository;
    }

    /**
     * 添加/修改
     */
    public Long saveOrUpdate(SeriesTopicEntity entity) {
        return seriesTopicRepository.saveOrUpdate(entity);
    }

    /**
     * 删除
     */
    public Boolean deleteById(Long id) {
        return seriesTopicRepository.deleteById(id);
    }

    /**
     * 列表
     */
    public List<SeriesTopicEntity> findByList() {
        return seriesTopicRepository.findByList();
    }

    public SeriesTopicEntity findById(Long id) {
        return seriesTopicRepository.findById(id);
    }

    public List<SeriesTopicEntity> findByIds(List<Long> topicIds) {
        return seriesTopicRepository.findByIds(topicIds);
    }
}
