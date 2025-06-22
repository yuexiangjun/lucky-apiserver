package com.lucky.domain.repository;

import com.lucky.domain.entity.LogisticsOrderPrizeEntity;

import java.util.List;

/**
 * 物流订单关联商品
 */

public interface LogisticsOrderPrizeRepository {

    Boolean savaOrUpdateBatch(List<LogisticsOrderPrizeEntity> logisticsOrderPrizes);

    List<LogisticsOrderPrizeEntity> getByLogisticsOrderIds(List<Long> logisticsOrderIds);
}
