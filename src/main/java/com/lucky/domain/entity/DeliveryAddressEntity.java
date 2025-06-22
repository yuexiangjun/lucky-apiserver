package com.lucky.domain.entity;

import lombok.Data;

/**
 * 收货地址信息
 */
@Data
public class DeliveryAddressEntity {
    /**
     * id
     */
    private Long id;
    /**
     * 用户id
     */
    private Long wechatUserId;
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
}
