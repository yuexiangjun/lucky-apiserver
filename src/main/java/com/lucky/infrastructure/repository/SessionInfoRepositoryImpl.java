package com.lucky.infrastructure.repository;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lucky.domain.entity.SessionInfoEntity;
import com.lucky.domain.repository.SessionInfoRepository;
import com.lucky.domain.valueobject.BaseDataPage;
import com.lucky.infrastructure.repository.mysql.mapper.SessionInfoMapper;
import com.lucky.infrastructure.repository.mysql.po.SessionInfoPO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 场次
 */
@Component
public class SessionInfoRepositoryImpl extends ServiceImpl<SessionInfoMapper, SessionInfoPO> implements SessionInfoRepository {
	private final SessionInfoMapper sessionInfoMapper;

	public SessionInfoRepositoryImpl(SessionInfoMapper sessionInfoMapper) {
		this.sessionInfoMapper = sessionInfoMapper;
	}


	/**
	 * 添加/修改
	 */
	public Long saveOrUpdate(SessionInfoEntity entity) {

		var gradePO = SessionInfoPO.getInstance(entity);

		if (Objects.isNull(gradePO))
			return null;

		if (Objects.isNull(gradePO.getId()))
			sessionInfoMapper.insert(gradePO);
		else
			sessionInfoMapper.updateById(gradePO);
		return gradePO.getId();
	}

	@Override
	public Boolean saveList(List<SessionInfoEntity> sessionInfoEntities) {

		var sessionInfoPOS = sessionInfoEntities.stream()
				.map(SessionInfoPO::getInstance)
				.collect(Collectors.toList());

		return this.saveBatch(sessionInfoPOS);
	}

	@Override
	public List<SessionInfoEntity> findByTopicId(Long topicId) {
		var eq = Wrappers.<SessionInfoPO>lambdaQuery()
				.eq(SessionInfoPO::getTopicId, topicId);
		return this.list(eq)
				.stream()
				.map(SessionInfoPO::toEntity)
				.collect(Collectors.toList());
	}

	@Override
	public Boolean deleteByTopicId(Long topicId) {
		var eq = Wrappers.<SessionInfoPO>lambdaQuery()
				.eq(SessionInfoPO::getTopicId, topicId);
		return this.remove(eq);
	}

	@Override
	public BaseDataPage<SessionInfoEntity> findByTopicIdPage(Long topicId, Integer page, Integer size) {
		var sessionInfoPOPage = new com.baomidou.mybatisplus.extension.plugins.pagination.Page<SessionInfoPO>(page, size);
		var eq = Wrappers.<SessionInfoPO>lambdaQuery()
				.eq(SessionInfoPO::getTopicId, topicId)
				.orderByAsc(SessionInfoPO::getSessionNumber);
		var page1 = this.page(sessionInfoPOPage, eq);


		return BaseDataPage.newInstance(
				page1.getTotal(),
				page1.getPages(),
				page1.getRecords()
						.stream()
						.map(SessionInfoPO::toEntity)
						.collect(Collectors.toList()));

	}

	@Override
	public BaseDataPage<SessionInfoEntity> findByTopicIdPageStatus(Long topicId, Integer page, Integer size) {
		var sessionInfoPOPage = new com.baomidou.mybatisplus.extension.plugins.pagination.Page<SessionInfoPO>(page, size);
		var eq = Wrappers.<SessionInfoPO>lambdaQuery()
				.eq(SessionInfoPO::getTopicId, topicId)
				.orderByAsc(SessionInfoPO::getStatus)
				.orderByAsc(SessionInfoPO::getSessionNumber);
		var page1 = this.page(sessionInfoPOPage, eq);


		return BaseDataPage.newInstance(
				page1.getTotal(),
				page1.getPages(),
				page1.getRecords()
						.stream()
						.map(SessionInfoPO::toEntity)
						.collect(Collectors.toList()));
	}

	@Override
	public SessionInfoEntity findById(Long sessionId) {
		if (Objects.isNull(sessionId))
			return null;
		var po = this.getById(sessionId);
		return SessionInfoPO.toEntity(po);
	}


}
