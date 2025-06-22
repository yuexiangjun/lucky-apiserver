package com.lucky.domain.entity;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 物流订单
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LogisticsOrderEntity {
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
    private DeliveryAddressEntity address;
    /**
     * 商品
     */
    private List<LogisticsOrderPrizeEntity> goods;

}
