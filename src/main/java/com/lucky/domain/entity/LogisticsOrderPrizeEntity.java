package com.lucky.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 物流订单关联商品
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LogisticsOrderPrizeEntity {
    /**
     * id
     */
    private Long id;
    /**
     * 商品id
     */
    private Long productId;
    /**
     * 订单商品id
     */
    private Long orderPrizeId;

    /**
     * 物流订单id
     */
    private Long logisticsOrderId;
}
