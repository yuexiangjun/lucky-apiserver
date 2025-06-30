package com.lucky.api.controller.admin.vo;

import cn.hutool.core.bean.BeanUtil;
import com.lucky.domain.valueobject.Metrics;
import lombok.Data;

import java.util.Objects;

@Data
public class MetricsVO {
    /**
     * 用户总数
     */
    private Long userCount;
    /**
     * 用户新增数
     */
    private Long userAddCount;
    /**
     * 订单总数
     */
    private Long orderCount;
    /**
     * 订单金额
     */
    private Long orderAmount;

    public static MetricsVO getInstance(Metrics metrics) {

        if (Objects.isNull(metrics))
            return null;
        return BeanUtil.toBean(metrics, MetricsVO.class);
    }

}
