package com.lucky.infrastructure.repository;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lucky.domain.entity.OrderEntity;
import com.lucky.domain.repository.OrderRepository;
import com.lucky.domain.valueobject.BaseDataPage;
import com.lucky.infrastructure.repository.mysql.mapper.OrderMapper;
import com.lucky.infrastructure.repository.mysql.po.OrderPO;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class OrderRepositoryImpl extends ServiceImpl<OrderMapper, OrderPO> implements OrderRepository {


	@Override
	public Long saveOrUpdate(OrderEntity entity) {
		var po = OrderPO.getInstance(entity);
		if (Objects.isNull(po))
			return null;
		this.saveOrUpdate(po);
		return po.getId();
	}

	@Override
	public List<OrderEntity> list(OrderEntity entity) {
		var wrapper = Wrappers.lambdaQuery(OrderPO.class)
				.eq(Objects.nonNull(entity.getWechatUserId()), OrderPO::getWechatUserId, entity.getWechatUserId())
				.eq(Objects.nonNull(entity.getTopicId()), OrderPO::getTopicId, entity.getTopicId())
				.eq(Objects.nonNull(entity.getSessionId()), OrderPO::getSessionId, entity.getSessionId())
				.eq(Objects.nonNull(entity.getStatus()), OrderPO::getStatus, entity.getStatus())
				.orderByDesc(OrderPO::getCreateTime);

		return this.list(wrapper)
				.stream()
				.map(OrderPO::toEntity)
				.collect(Collectors.toList());


	}

	@Override
	public OrderEntity getById(Long id) {
		var wrapper = Wrappers.lambdaQuery(OrderPO.class)
				.eq(OrderPO::getId, id);
		var one = this.getOne(wrapper);
		if (Objects.nonNull(one))
			return OrderPO.toEntity(one);
		return null;

	}

	@Override
	public BaseDataPage<OrderEntity> page(OrderEntity entity, Integer page, Integer size) {

		var orderPOPage = new com.baomidou.mybatisplus.extension.plugins.pagination.Page<OrderPO>(page, size);

		var wrapper = Wrappers.lambdaQuery(OrderPO.class)
				.eq(Objects.nonNull(entity.getWechatUserId()), OrderPO::getWechatUserId, entity.getWechatUserId())
				.eq(Objects.nonNull(entity.getTopicId()), OrderPO::getTopicId, entity.getTopicId())
				.eq(Objects.nonNull(entity.getSessionId()), OrderPO::getSessionId, entity.getSessionId())
				.eq(Objects.nonNull(entity.getStatus()), OrderPO::getStatus, entity.getStatus())
				.orderByDesc(Objects.nonNull(entity.getStatus()) && Objects.equals(entity.getStatus(), 2), OrderPO::getFinishTime)
				.orderByDesc(Objects.nonNull(entity.getStatus()) && Objects.equals(entity.getStatus(), 1), OrderPO::getSendTime)
				.orderByDesc(Objects.nonNull(entity.getStatus()) && Objects.equals(entity.getStatus(), 0), OrderPO::getCreateTime)
				.orderByDesc(Objects.isNull(entity.getStatus()), OrderPO::getCreateTime);

		Page<OrderPO> page1 = this.page(orderPOPage, wrapper);


		return BaseDataPage.newInstance(
				page1.getTotal(),
				page1.getPages(),
				page1.getRecords()
						.stream()
						.map(OrderPO::toEntity)
						.collect(Collectors.toList()));

	}

	@Override
	public Boolean saveBatch(List<OrderEntity> orderEntities) {
		if (CollectionUtils.isEmpty(orderEntities))
			return false;
		var orderPOS = orderEntities.stream()
				.map(OrderPO::getInstance)
				.collect(Collectors.toList());
		return this.saveBatch(orderPOS);

	}

	@Override
	public void getByWechatUserId(Long wechatUserId) {



	}
}
