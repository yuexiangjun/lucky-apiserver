package com.lucky.domain.valueobject;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PayOrderReturnValue {
    /**
     * 订单id
     */
    private Long payOrderId;
    /**
     * 支付结果
     */
    private String payResult;
    /**
     * 支付三方流水
     */
    private String thirdPayId;


}
