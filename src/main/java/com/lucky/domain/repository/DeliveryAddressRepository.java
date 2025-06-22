package com.lucky.domain.repository;

import com.lucky.domain.entity.DeliveryAddressEntity;

import java.util.List;

/**
 * 收货地址信息
 */

public interface DeliveryAddressRepository {
    /**
     * 添加修改
     *
     * @param entity
     * @return
     */
    Long saveOrUpdate(DeliveryAddressEntity entity);

    /**
     * 根据id查询
     */
    DeliveryAddressEntity getById(Long id);

    /**
     * 更据id删除
     */
    Boolean deleteById(Long id);

    /**
     * 更据用户查询
     */
    List<DeliveryAddressEntity> getByWechatUserId(Long wechatUserId);
    /**
     * 批量修改
     */
    Boolean saveOrUpdateBatch(List<DeliveryAddressEntity> entityList);

}
