package com.lucky.domain.valueobject;

import cn.hutool.core.bean.BeanUtil;
import com.lucky.domain.entity.PrizeInfoEntity;
import lombok.Data;

import java.util.Objects;

@Data
public class SuccessProducts {
    /**
     * id
     */
    private Long id;
    /**
     * 奖品等级
     */
    private String gradeName;
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
    private Long num;

    public static SuccessProducts getInstance(PrizeInfoEntity entity, String gradeName, Long num) {
        if (Objects.isNull(entity))
            return null;
        var bean = BeanUtil.toBean(entity, SuccessProducts.class);
        bean.setGradeName(gradeName);
        bean.setNum(num);
        return bean;
    }

}
