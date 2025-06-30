package com.lucky.domain.valueobject;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Metrics {
    /**
     * 用户总数
     */
    private  Integer userCount;
    /**
     * 用户新增数
     */
    private Integer userAddCount;
    /**
     * 订单总数
     */
    private Integer orderCount;
    /**
     * 订单金额
     */
    private BigDecimal orderAmount;

}
