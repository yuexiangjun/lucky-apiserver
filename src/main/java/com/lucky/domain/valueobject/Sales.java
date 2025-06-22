package com.lucky.domain.valueobject;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Sales {
    /**
     * 主题名称
     */
    private String topicName;
    /**
     * 销售商品总价值
     */
    private BigDecimal productTotalValue;
    /**
     * 销售单量
     */
    private Integer orderNumber;
    /**
     * 销售金额
     */
    private BigDecimal salesAmount;
    /**
     * 实际利润
     */
    private BigDecimal actualProfit;

}
