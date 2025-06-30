package com.lucky.infrastructure.repository;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lucky.domain.entity.BalanceLogEntity;
import com.lucky.domain.repository.BalanceLogRepository;
import com.lucky.infrastructure.repository.mysql.mapper.BalanceLogMapper;
import com.lucky.infrastructure.repository.mysql.po.BalanceLogPO;
import org.springframework.stereotype.Component;

@Component
public class BalanceLogRepositoryImpl extends ServiceImpl<BalanceLogMapper, BalanceLogPO> implements BalanceLogRepository {


	@Override
	public void saveOrUpdate(BalanceLogEntity balanceLogEntity) {
		var bean = BalanceLogPO.getInstance(balanceLogEntity);
		this.saveOrUpdate(bean);

	}
}
