package com.lucky.domain.repository;

import com.lucky.domain.entity.NoCounterEntity;
import com.lucky.domain.enumes.NoCounterType;

public interface NoCounterRepository {
	NoCounterEntity getNoCounterPo(NoCounterType type);

	void saveOrUpdate(NoCounterEntity entity);
}
