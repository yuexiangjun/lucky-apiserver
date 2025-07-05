package com.lucky.api.controller.admin.vo;

import cn.hutool.core.bean.BeanUtil;
import com.lucky.domain.entity.BannerEntity;
import lombok.Data;


@Data
public class BannerVO {
    /**
     * id
     */
    private Long id;
    /**
     * banner名称
     */
    private String name;
    /**
     * banner图片
     */
    private String image;
    /**
     * 排序
     */
    private Integer sort;
    /**
     * 启用禁用
     */
    private Boolean enabled;

    public static BannerVO getInstance(BannerEntity entity) {

        if (entity == null)
            return null;
        return BeanUtil.toBean(entity, BannerVO.class);

    }


}
