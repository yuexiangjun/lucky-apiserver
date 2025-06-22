package com.lucky.domain;

import com.lucky.domain.entity.PayOrderEntity;
import com.lucky.domain.repository.PayOrderRepository;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class PayOrderService {
	private final PayOrderRepository payOrderRepository;

	public PayOrderService(PayOrderRepository payOrderRepository) {
		this.payOrderRepository = payOrderRepository;
	}
	/**
	 * 添加 修改
	 */
	public Long saveOrUpdate(PayOrderEntity entity) {
		return payOrderRepository.saveOrUpdate(entity);
	}
	/**
	 * 更据id 查询
	 */
	public PayOrderEntity getById(Long id) {
		if (Objects.isNull(id))
			return null;
		return payOrderRepository.getById(id);
	}

}
