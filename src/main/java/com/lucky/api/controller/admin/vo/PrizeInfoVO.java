package com.lucky.api.controller.admin.vo;

import cn.hutool.core.bean.BeanUtil;
import com.lucky.domain.entity.PrizeInfoEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * 奖品
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PrizeInfoVO {
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
     * 奖品等级
     */
    private Long gradeId;
    /**
     * 主题id
     */
    private Long topicId;
    /**
     * 奖品名称
     */
    private String prizeName;
    /**
     * 奖品图片
     */
    private String prizeUrl;
    /**
     * 库存
     */
    private Integer inventory;
    /**
     * 单价
     */
    private BigDecimal price;
    public static  PrizeInfoVO getInstance(PrizeInfoEntity entity){
        if(Objects.isNull(entity))
            return null;
        return BeanUtil.toBean(entity,PrizeInfoVO.class);
    }
}
