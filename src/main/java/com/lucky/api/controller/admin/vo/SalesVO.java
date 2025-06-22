package com.lucky.api.controller.admin.vo;
import cn.hutool.core.bean.BeanUtil;
import com.lucky.domain.valueobject.Sales;
import lombok.Data;

import java.math.BigDecimal;


@Data
public class SalesVO {
    /**
     * 主题名称
     */
    private String topicName;
    /**
     * 商品总价值
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

    public static SalesVO getInstance(Sales sales) {
        if (sales == null)
            return null;
        return BeanUtil.toBean(sales, SalesVO.class);

    }

}
