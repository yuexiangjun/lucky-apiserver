package com.lucky.domain;

import cn.hutool.core.bean.BeanUtil;
import com.lucky.domain.entity.SessionInfoEntity;
import com.lucky.domain.repository.SessionInfoRepository;
import com.lucky.domain.valueobject.BaseDataPage;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 场次
 */
@Component
public class SessionInfoService {
	private final SessionInfoRepository sessionInfoRepository;

	public SessionInfoService(SessionInfoRepository sessionInfoRepository) {
		this.sessionInfoRepository = sessionInfoRepository;
	}

	/**
	 * 添加/修改
	 */
	public Long saveOrUpdate(SessionInfoEntity entity) {
		return sessionInfoRepository.saveOrUpdate(entity);
	}

	@Transactional(rollbackFor = Exception.class)
	public Boolean addSession(SessionInfoEntity sessionInfoEntity, Integer number, Long topicId) {

		if (Objects.nonNull(topicId))
			sessionInfoRepository.deleteByTopicId(topicId);

		List<SessionInfoEntity> sessionInfoEntities = new ArrayList<>();

		for (Integer i = 0; i < number; i++) {
			SessionInfoEntity bean = BeanUtil.toBean(sessionInfoEntity, SessionInfoEntity.class);
			bean.setSessionNumber(i + 1);
			sessionInfoEntities.add(bean);
		}

		return sessionInfoRepository.saveList(sessionInfoEntities);
	}

	/**
	 * 根据主题查询场次
	 */
	public List<SessionInfoEntity> findByTopicId(Long topicId) {
		return sessionInfoRepository.findByTopicId(topicId);
	}
	/**
	 *根据主题查询场次 分页
	 * 更据编号排序
	 */
	public BaseDataPage<SessionInfoEntity> findByTopicIdPageNO(Long topicId, Integer page, Integer size) {
		return sessionInfoRepository.findByTopicIdPage(topicId,page,size);
	}
	/**
	 *根据主题查询场次 分页
	 * 更据状态
	 */
	public BaseDataPage<SessionInfoEntity> findByTopicIdPageStatus(Long topicId, Integer page, Integer size) {
		return sessionInfoRepository.findByTopicIdPageStatus(topicId,page,size);
	}


	public SessionInfoEntity findById(Long sessionId) {
		return	sessionInfoRepository	.findById(sessionId);
	}
}
