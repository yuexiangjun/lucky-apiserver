package com.lucky.api.controller.external.vo;

import cn.hutool.core.bean.BeanUtil;
import com.lucky.domain.entity.CustomerInfoEntity;
import lombok.Data;

import java.util.Objects;

/**
 * 客服信息
 */
@Data
public class CustomerInfoVO {
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


    public static CustomerInfoVO getInstance(CustomerInfoEntity entity) {

        if (Objects.isNull(entity))
            return null;

        return BeanUtil.toBean(entity, CustomerInfoVO.class);

    }
}
