package com.lucky.infrastructure.repository;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lucky.domain.entity.PayOrderEntity;
import com.lucky.domain.repository.PayOrderRepository;
import com.lucky.infrastructure.repository.mysql.mapper.PayOrderMapper;
import com.lucky.infrastructure.repository.mysql.po.PayOrderPO;
import org.springframework.stereotype.Component;

import java.util.Objects;

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
}
