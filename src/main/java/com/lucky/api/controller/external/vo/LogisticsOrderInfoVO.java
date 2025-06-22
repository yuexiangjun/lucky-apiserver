package com.lucky.api.controller.external.vo;


import cn.hutool.core.bean.BeanUtil;
import com.lucky.domain.valueobject.LogisticsOrderInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LogisticsOrderInfoVO {
    /**
     * 订单id
     */
    private Long id;
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
     * 物流公司
     */
    private String logisticsCompany;
    /**
     * 订单创建时间
     */
    private LocalDateTime createTime;
    /**
     * 商品信息
     */
    private List<WechatPrizeInfoVO> goods;

    public static LogisticsOrderInfoVO getInstance(LogisticsOrderInfo entity) {
        if (Objects.isNull(entity))
            return null;
        var bean = BeanUtil.toBean(entity, LogisticsOrderInfoVO.class);

        bean.setGoods(entity.getGoods().stream().map(WechatPrizeInfoVO::getInstance).toList());
        return bean;

    }


}
