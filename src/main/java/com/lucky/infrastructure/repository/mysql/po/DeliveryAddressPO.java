package com.lucky.infrastructure.repository.mysql.po;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.lucky.domain.entity.DeliveryAddressEntity;
import lombok.Data;

import java.util.Objects;

/**
 * 收货地址信息
 */
@Data
@TableName("delivery_address")
public class DeliveryAddressPO {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
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

    public static DeliveryAddressPO getInstance(DeliveryAddressEntity entity) {
        if (Objects.isNull(entity))
            return null;
        return BeanUtil.toBean(entity, DeliveryAddressPO.class);
    }

    public static DeliveryAddressEntity toEntity(DeliveryAddressPO po) {
        if (Objects.isNull(po))
            return null;
        return BeanUtil.toBean(po, DeliveryAddressEntity.class);
    }
}
