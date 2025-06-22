package com.lucky.domain.valueobject;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
@Data
@Builder
@AllArgsConstructor
public class PayOrderPram {
    /**
     * 订单id
     */
    private Long payOrderId;
    /**
     * 金额 （单位元）
     */
    private BigDecimal payMoney;
    /**
     * 支付人
     */
    private  String openId;
    /**
     * 商品描述
     */
    private String payDesc;
    public static PayOrderPram getInstance(Long payOrderId,
                                           BigDecimal payMoney,
                                           String openId){
        return PayOrderPram.builder()
                .payOrderId(payOrderId)
                .payMoney(payMoney)
                .openId(openId)
                .payDesc("福星抽奖支付订单")
                .build();
    }


}
