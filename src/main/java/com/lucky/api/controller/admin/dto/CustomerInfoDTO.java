package com.lucky.api.controller.admin.dto;

import cn.hutool.core.bean.BeanUtil;
import com.lucky.domain.entity.CustomerInfoEntity;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 客服信息
 */
@Data
public class CustomerInfoDTO {
    /**
     * id
     */
    private Long id;
    /**
     * 客服名称
     */
    private String name;
    /**
     * 客服电话
     */
    private String phone;
    /**
     * 客服微信
     */
    private String wechat;


    public static CustomerInfoEntity toEntity(CustomerInfoDTO entity) {

        if (Objects.isNull(entity))
            return null;

        CustomerInfoEntity bean = BeanUtil.toBean(entity, CustomerInfoEntity.class);
        if (Objects.nonNull(bean.getId()))
            bean.setCreateTime(LocalDateTime.now());
        else
            bean.setUpdateTime(LocalDateTime.now());
        return bean;

    }
}
