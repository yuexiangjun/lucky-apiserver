package com.lucky.application;

import com.lucky.domain.GradeService;
import com.lucky.domain.OrderService;
import com.lucky.domain.PrizeInfoService;
import com.lucky.domain.SessionInfoService;
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
	private final OrderService orderService;
	private final SessionInfoService sessionInfoService;

	public PrizeInfoServer(PrizeInfoService prizeInfoService, GradeService gradeService, OrderService orderService, SessionInfoService sessionInfoService) {
		this.prizeInfoService = prizeInfoService;
		this.gradeService = gradeService;
		this.orderService = orderService;
		this.sessionInfoService = sessionInfoService;
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

	@Transactional(rollbackFor = Exception.class)
	public Boolean updatePrize(PrizeInfoEntity entity) {

		if (Objects.isNull(entity.getTopicId()))
			throw BusinessException.newInstance("奖品所属系列为空");

		//查询该奖品的等级奖项
		Long id = entity.getId();
		var prizeInfo = prizeInfoService.findById(id);
		if (Objects.isNull(prizeInfo))
			throw BusinessException.newInstance("修改奖品不存在");

		var gradeEntity = gradeService.findById(prizeInfo.getGradeId());

		if (Objects.isNull(gradeEntity))
			throw BusinessException.newInstance("奖品等级不存在");

		if (!Objects.equals(gradeEntity.getEditable(), 1))
			throw BusinessException.newInstance("奖品等级不可修改");

//		//查询该奖品是抽取过了
//
//		if (orderService.boxCabinets(entity.getTopicId(), id))
//			throw BusinessException.newInstance("奖品已抽取，不能修改");

		entity.setId(null);

		var prizeInfoId = prizeInfoService.saveOrUpdate(entity);

		prizeInfo.setReplacePrizeId(prizeInfoId);
		prizeInfoService.saveOrUpdate(prizeInfo);


		//修改场次中的奖品

		return sessionInfoService.updatePrize(entity.getTopicId(), id, prizeInfoId);

	}
}
