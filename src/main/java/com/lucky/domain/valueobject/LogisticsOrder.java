package com.lucky.domain.valueobject;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class LogisticsOrder {
    /**
     * 地址id
     */
    private Long addressId;
    /**
     * 商品
     */
    private List<PrizeInfoNum> goods;
    /**
     * 用户
     */
    private Long wechatUserId;
    /**
     * 快递费
     */
    private BigDecimal expressMoney;
    /**
     * 支付类型 1 微信支付 2：平台积分支付
     */
    private Integer payType;
}
