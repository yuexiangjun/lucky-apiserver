package com.lucky.api.controller.external.vo;

import cn.hutool.core.bean.BeanUtil;
import com.lucky.domain.entity.DeliveryAddressEntity;
import lombok.Data;

import java.util.Objects;

/**
 * 收货地址信息
 */
@Data
public class DeliveryAddressVO {
    /**
     * id
     */
    private Long id;
    /**
     * 收货人名
     */
    private String name;
    /**
     * 收货人手机号
     */
    private String phone;
    /**
     * 省
     */
    private String province;
    /**
     * 市
     */
    private String city;
    /**
     * 区
     */
    private String area;
    /**
     * 收货人地址
     */
    private String address;
    /**
     * 是否默认
     */
    private Boolean isDefault;
    public static DeliveryAddressVO getInstance(DeliveryAddressEntity entity) {

        if (Objects.isNull(entity))
            return null;

        return BeanUtil.toBean(entity, DeliveryAddressVO.class);

    }
}
