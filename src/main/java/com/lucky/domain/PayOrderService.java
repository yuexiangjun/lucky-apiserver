package com.lucky.domain;

import com.lucky.domain.entity.PayOrderEntity;
import com.lucky.domain.repository.PayOrderRepository;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
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

	public List<PayOrderEntity> listByTime(PayOrderEntity payOrderEntity, LocalDateTime startTime, LocalDateTime endTime) {
		return payOrderRepository.listByTime(payOrderEntity, startTime, endTime);

	}

	public List<PayOrderEntity> findByWechatUserIds(List<Long> wechatUserIds) {
		if (CollectionUtils.isEmpty(wechatUserIds)) {
			return List.of();
		}

		return payOrderRepository.findByWechatUserIds(wechatUserIds);

	}

	/**
	 * 获取当前用户的消费金额
	 *
	 * @param wechatUserId
	 * @return
	 */
	public BigDecimal findByWechatUserIdAndPayMoney(Long wechatUserId) {
		var byWechatUserId = payOrderRepository.findByWechatUserId(wechatUserId);

		return byWechatUserId.stream()
				.map(PayOrderEntity::getPayMoney)
				.reduce(BigDecimal.ZERO, BigDecimal::add);

	}

	public  List<PayOrderEntity> findByIdsList(List<Long> payOrderIds) {
		if (CollectionUtils.isEmpty(payOrderIds))
			return List.of();
		return payOrderRepository.findByIdsList(payOrderIds);

	}
}
