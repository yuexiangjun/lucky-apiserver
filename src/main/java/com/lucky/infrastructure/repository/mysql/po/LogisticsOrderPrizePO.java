package com.lucky.infrastructure.repository.mysql.po;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.lucky.domain.entity.LogisticsOrderPrizeEntity;
import lombok.Data;

import java.util.Objects;

/**
 * 物流订单关联商品
 */
@Data
@TableName("logistics_order_prize")
public class LogisticsOrderPrizePO {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
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

    public static LogisticsOrderPrizePO getInstance(LogisticsOrderPrizeEntity entity) {
        if (Objects.isNull(entity))
            return null;
        return BeanUtil.toBean(entity, LogisticsOrderPrizePO.class);
    }

    public static LogisticsOrderPrizeEntity toEntity(LogisticsOrderPrizePO po) {
        if (Objects.isNull(po))
            return null;
        return BeanUtil.toBean(po, LogisticsOrderPrizeEntity.class);
    }
}
