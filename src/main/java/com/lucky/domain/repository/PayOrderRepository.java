package com.lucky.domain.repository;

import com.lucky.domain.entity.PayOrderEntity;

public interface PayOrderRepository {
	Long saveOrUpdate(PayOrderEntity entity);

	PayOrderEntity getById(Long id);
}
