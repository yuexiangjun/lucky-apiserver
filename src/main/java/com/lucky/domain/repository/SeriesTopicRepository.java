package com.lucky.domain.repository;

import com.lucky.domain.entity.SeriesTopicEntity;
import com.lucky.domain.valueobject.BaseDataPage;

import java.util.List;

public interface SeriesTopicRepository {
    Long saveOrUpdate(SeriesTopicEntity entity);

    Boolean deleteById(Long id);

    List<SeriesTopicEntity> findByList(SeriesTopicEntity seriesTopicEntity);
     BaseDataPage<SeriesTopicEntity> findByListPage(SeriesTopicEntity seriesTopicEntity, Integer page, Integer size);

    SeriesTopicEntity findById(Long id);

    List<SeriesTopicEntity> findByIds(List<Long> topicIds);

	List<SeriesTopicEntity> getByName(String seriesName);
}
