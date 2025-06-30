package com.lucky.domain.repository;

import com.lucky.domain.entity.PayOrderEntity;

import java.time.LocalDateTime;
import java.util.List;

import java.util.List;

public interface PayOrderRepository {
	Long saveOrUpdate(PayOrderEntity entity);

	PayOrderEntity getById(Long id);
    List<PayOrderEntity> findByWechatUserIds(List<Long> wechatUserIds);

	List<PayOrderEntity> listByTime(PayOrderEntity payOrderEntity, LocalDateTime startTime, LocalDateTime endTime);
}
