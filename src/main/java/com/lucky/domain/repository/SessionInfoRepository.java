package com.lucky.domain.repository;

import com.lucky.domain.entity.SessionInfoEntity;
import com.lucky.domain.valueobject.BaseDataPage;


import java.util.List;

public interface SessionInfoRepository {
    Long saveOrUpdate(SessionInfoEntity entity);


    Boolean saveOrUpdateBatch(List<SessionInfoEntity> sessionInfoEntities);

    List<SessionInfoEntity> findByTopicId(Long topicId);

    Boolean deleteByTopicId(Long topicId);

    BaseDataPage<SessionInfoEntity> findByTopicIdPage(Long topicId, Integer page, Integer size);

    BaseDataPage<SessionInfoEntity> findByTopicIdPageStatus(Long topicId, Integer page, Integer size);

	SessionInfoEntity findById(Long sessionId);
}
