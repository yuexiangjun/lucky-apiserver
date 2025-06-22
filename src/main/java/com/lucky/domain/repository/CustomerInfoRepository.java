package com.lucky.domain.repository;

import com.lucky.domain.entity.CustomerInfoEntity;

import java.util.List;

/**
 * 客服信息
 */

public interface CustomerInfoRepository {
    /**
     *添加修改
     */
    Long saveOrUpdate(CustomerInfoEntity entity);
    /**
     * 查询所有
     */
    List<CustomerInfoEntity> getList();


}
