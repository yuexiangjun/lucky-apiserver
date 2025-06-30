package com.lucky.domain.repository;

import com.lucky.domain.entity.BalanceLogEntity;

public interface BalanceLogRepository {
	void saveOrUpdate(BalanceLogEntity balanceLogEntity);
}
