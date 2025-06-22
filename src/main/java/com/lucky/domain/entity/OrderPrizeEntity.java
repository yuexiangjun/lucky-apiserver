package com.lucky.domain.entity;

import lombok.*;

import java.time.LocalDateTime;

/**
 * 订单关联商品
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderPrizeEntity {
    /**
     * id
     */
    private Long id;
    /**
     * 微信用户id
     */
    private Long wechatUserId;
    /**
     * 订单id
     */
    private Long orderId;
    /**
     * 商品id
     */
    private Long productId;
    /**
     * 场次id
     */
    private Long sessionId;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 是否发货
     */
    private  Boolean isDelivery;


}
