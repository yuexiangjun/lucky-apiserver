package com.lucky.api.controller.admin.dto;

import cn.hutool.core.bean.BeanUtil;
import com.lucky.domain.entity.LogisticsOrderEntity;
import lombok.Data;

import java.util.Objects;

@Data
public class LogisticsOrderInfoDTO {
    /**
     * 订单状态
     */
    private Integer status;
    /**
     * 订单编号
     */
    private String number;

    /**
     * 快递单号
     */
    private String logisticsNumber;

    /**
     * 手机号码
     */
    private String phone;


    /**
     *
     * @param dto
     * @return
     */

    public static LogisticsOrderEntity toEntity(LogisticsOrderInfoDTO dto ) {
        if (Objects.isNull(dto))
            return null;
       return BeanUtil.toBean(dto, LogisticsOrderEntity.class);


    }
}
