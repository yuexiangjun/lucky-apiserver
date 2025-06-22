package com.lucky.domain.valueobject;

import cn.hutool.core.bean.BeanUtil;
import com.lucky.domain.entity.PrizeInfoEntity;
import lombok.*;

import java.util.Objects;

@Getter
@Setter

@AllArgsConstructor
@NoArgsConstructor
//@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
public class Inventory {
    /**
     * 奖品id
     */
    private Long prizeId;
    /**
     * 库存
     */
    private Integer inventory;

    public static Inventory getInstance(PrizeInfoEntity prizeInfoEntity) {
        if (Objects.isNull(prizeInfoEntity))
            return null;
        Inventory bean = BeanUtil.toBean(prizeInfoEntity, Inventory.class);
        bean.setPrizeId(prizeInfoEntity.getId());
        return bean;

    }

}
