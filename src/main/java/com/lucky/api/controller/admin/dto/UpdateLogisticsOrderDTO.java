package com.lucky.api.controller.admin.dto;

import cn.hutool.core.bean.BeanUtil;
import com.lucky.domain.entity.LogisticsOrderEntity;
import lombok.Data;

import java.util.Objects;

@Data
public class UpdateLogisticsOrderDTO {
    /**
     * id
     */
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
     * 订单状态 1：待发送 2：已完成
     */
    private Integer status;
    public static LogisticsOrderEntity toEntity(UpdateLogisticsOrderDTO dto ) {
        if (Objects.isNull(dto))
            return null;
        return BeanUtil.toBean(dto, LogisticsOrderEntity.class);


    }
}