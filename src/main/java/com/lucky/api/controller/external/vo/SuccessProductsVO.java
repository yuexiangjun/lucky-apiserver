package com.lucky.api.controller.external.vo;

import cn.hutool.core.bean.BeanUtil;
import com.lucky.domain.valueobject.SuccessProducts;
import lombok.Data;

import java.util.Objects;

@Data
public class SuccessProductsVO {
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

    public static SuccessProductsVO getInstance(SuccessProducts entity) {
        if (Objects.isNull(entity))
            return null;
        return BeanUtil.toBean(entity, SuccessProductsVO.class);
    }

}
