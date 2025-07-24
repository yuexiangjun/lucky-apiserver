package com.lucky.infrastructure.repository;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lucky.domain.entity.PayOrderEntity;
import com.lucky.domain.repository.PayOrderRepository;
import com.lucky.infrastructure.repository.mysql.mapper.PayOrderMapper;
import com.lucky.infrastructure.repository.mysql.po.PayOrderPO;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class PayOrderRepositoryImpl extends ServiceImpl<PayOrderMapper, PayOrderPO> implements PayOrderRepository {
	@Override
	public Long saveOrUpdate(PayOrderEntity entity) {
		var po = PayOrderPO.getInstance(entity);
		this.saveOrUpdate(po);

		return po.getId();
	}

	@Override
	public PayOrderEntity getById(Long id) {
		if (Objects.isNull(id))
			return null;
		var wrapper = Wrappers.lambdaQuery(PayOrderPO.class)
				.eq(PayOrderPO::getId, id);
		var one = this.getOne(wrapper);
		if (Objects.nonNull(one))
			return PayOrderPO.toEntity(one);
		return null;

	}

	@Override
	public List<PayOrderEntity> listByTime(PayOrderEntity entity, LocalDateTime startTime, LocalDateTime endTime) {
		var wrapper = Wrappers.lambdaQuery(PayOrderPO.class)
				.eq(Objects.nonNull(entity.getPayStatus()), PayOrderPO::getPayStatus, entity.getPayStatus())
				.eq(Objects.nonNull(entity.getOrderType()), PayOrderPO::getOrderType, entity.getOrderType())

				.ge(startTime != null, PayOrderPO::getPayTime, startTime)
				.le(endTime != null, PayOrderPO::getPayTime, endTime);

		return this.list(wrapper)
				.stream()
				.map(PayOrderPO::toEntity)
				.collect(Collectors.toList());
	}

	@Override
	public List<PayOrderEntity> findByWechatUserId(Long wechatUserId) {

		var wrapper = Wrappers.lambdaQuery(PayOrderPO.class)
				.eq( PayOrderPO::getPayStatus, 1)
				.in( PayOrderPO::getOrderType, List.of(1,3))
				.eq(PayOrderPO::getWechatUserId, wechatUserId);
		return this.list(wrapper)
				.stream()
				.map(PayOrderPO::toEntity)
				.collect(Collectors.toList());
	}

	@Override
	public List<PayOrderEntity> findByWechatUserIds(List<Long> wechatUserIds) {
		if (CollectionUtils.isEmpty(wechatUserIds))
			return List.of();

		var wrapper = Wrappers.lambdaQuery(PayOrderPO.class)
				.eq(PayOrderPO::getPayStatus, 1)
				.eq(PayOrderPO::getOrderType, 1)
				.in(PayOrderPO::getWechatUserId, wechatUserIds);

		var list = this.list(wrapper);

		return list.stream()
				.map(PayOrderPO::toEntity)
				.collect(Collectors.toList());

	}
}
