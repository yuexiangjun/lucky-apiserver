package com.lucky.infrastructure.repository.mysql.po;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.lucky.domain.entity.LogisticsOrderEntity;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 物流订单
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName(value = "logistics_order",autoResultMap = true)
public class LogisticsOrderPO {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;
    /**
     * 用户
     */
    private Long wechatUserId;
    /**
     * 编号
     */
    private String number;
    /**
     * 快递单号
     */
    private String logisticsNumber;
    /**
     * 快递公司
     */
    private String logisticsCompany;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
    /**
     * 订单状态 1：待发送 2：已完成
     */
    private Integer status;
    /**
     * 物流地址
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private DeliveryAddressPO address;

    public static LogisticsOrderPO getInstance(LogisticsOrderEntity entity) {
        if (Objects.isNull(entity))
            return null;
        var bean = BeanUtil.toBean(entity, LogisticsOrderPO.class);
        bean.setAddress(DeliveryAddressPO.getInstance(entity.getAddress()));
        if (Objects.isNull(bean.getId()))
            bean.setCreateTime(LocalDateTime.now());
        else
            bean.setUpdateTime(LocalDateTime.now());
        return bean;
    }

    public static LogisticsOrderEntity toEntity(LogisticsOrderPO po) {
        if (Objects.isNull(po))
            return null;
        var bean = BeanUtil.toBean(po, LogisticsOrderEntity.class);
        bean.setAddress(DeliveryAddressPO.toEntity(po.getAddress()));
        return bean;
    }

}
