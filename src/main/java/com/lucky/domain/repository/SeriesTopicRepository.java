package com.lucky.domain.repository;

import com.lucky.domain.entity.SeriesTopicEntity;

import java.util.List;

public interface SeriesTopicRepository {
    Long saveOrUpdate(SeriesTopicEntity entity);

    Boolean deleteById(Long id);

    List<SeriesTopicEntity> findByList();

    SeriesTopicEntity findById(Long id);

    List<SeriesTopicEntity> findByIds(List<Long> topicIds);
}
