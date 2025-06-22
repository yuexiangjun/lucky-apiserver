package com.lucky.api.controller.external.vo;

import cn.hutool.core.bean.BeanUtil;
import com.lucky.domain.entity.PrizeInfoEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

/**
 * 奖品
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WechatPrizeInfoVO {
    /**
     * id
     */
    private Long id;
    /**
     * 类别
     * 1:隐藏
     * 2：普通级别
     */
    private Integer type;

    /**
     * 奖品名称
     */
    private String prizeName;
    /**
     * 奖品图片
     */
    private String prizeUrl;

    /**
     * 数量
     */
    private Integer num;

    /**
     * @param entity
     * @return
     */
    public static WechatPrizeInfoVO getInstance(PrizeInfoEntity entity) {
        if (Objects.isNull(entity))
            return null;
        WechatPrizeInfoVO bean = BeanUtil.toBean(entity, WechatPrizeInfoVO.class);
        bean.setNum(entity.getInventory());
        return bean;
    }
}
