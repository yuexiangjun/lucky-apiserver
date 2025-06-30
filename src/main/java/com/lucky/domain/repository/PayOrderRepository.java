package com.lucky.domain.repository;

import com.lucky.domain.entity.PayOrderEntity;

import java.util.List;

public interface PayOrderRepository {
	Long saveOrUpdate(PayOrderEntity entity);

	PayOrderEntity getById(Long id);

	List<PayOrderEntity> findByWechatUserIds(List<Long> wechatUserIds);
}
