package com.lucky.domain.repository;

import com.lucky.domain.entity.OrderEntity;
import com.lucky.domain.valueobject.BaseDataPage;

import java.util.List;

public interface OrderRepository {
	Long saveOrUpdate(OrderEntity entity);

	List<OrderEntity> list(OrderEntity entity);

	OrderEntity getById(Long id);

	BaseDataPage<OrderEntity> page(OrderEntity entity, Integer page, Integer size);

	Boolean saveBatch(List<OrderEntity> orderEntities);

	void getByWechatUserId(Long wechatUserId);
}
