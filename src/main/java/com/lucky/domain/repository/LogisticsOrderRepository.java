package com.lucky.domain.repository;

import com.lucky.domain.entity.LogisticsOrderEntity;
import com.lucky.domain.valueobject.BaseDataPage;

import java.util.List;

/**
 * 物流订单
 */


public interface LogisticsOrderRepository {

    Long saveOrUpdate(LogisticsOrderEntity logisticsOrderEntity);

    List<LogisticsOrderEntity> getByWechatUserId(Long wechatUserId);

    List<LogisticsOrderEntity> getByAdminList(LogisticsOrderEntity entity);

    BaseDataPage<LogisticsOrderEntity> getByAdminListPage(LogisticsOrderEntity entity, Integer page, Integer size);
}
