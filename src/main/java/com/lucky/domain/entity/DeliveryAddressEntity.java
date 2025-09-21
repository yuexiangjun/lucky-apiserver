package com.lucky.domain.entity;

import lombok.Data;

import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.Collectors;

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

    public static String getAddressStr(DeliveryAddressEntity entity) {
        if (Objects.isNull(entity))
            return null;

        ArrayList<String> strings = new ArrayList<>();
        strings.add(entity.getProvince());
        strings.add(entity.getCity());
        strings.add(entity.getArea());
        strings.add(entity.getAddress());
        return strings.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.joining(""));


    }
    public static String getNameStr(DeliveryAddressEntity entity) {
        if (Objects.isNull(entity))
            return null;


        return entity.getName();


    }
}
