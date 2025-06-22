package com.lucky.domain.repository;

import com.lucky.domain.entity.LogisticsOrderEntity;

import java.util.List;

/**
 * 物流订单
 */


public interface LogisticsOrderRepository {

    Long saveOrUpdate(LogisticsOrderEntity logisticsOrderEntity);

    List<LogisticsOrderEntity> getByWechatUserId(Long wechatUserId);

    List<LogisticsOrderEntity> getByAdminList(LogisticsOrderEntity entity);
}
