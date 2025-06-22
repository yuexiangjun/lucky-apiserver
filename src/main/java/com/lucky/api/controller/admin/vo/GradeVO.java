package com.lucky.api.controller.admin.vo;

import com.lucky.domain.entity.GradeEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * 奖项
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GradeVO {
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
     * 排序
     */
    private Integer sort;
    /**
     * 名称
     */
    private String name;
    /**
     * 概率 隐藏类型的必传 普通级别的不用传
     */
    private BigDecimal probability;
    /**
     * 是否启用
     */
    private Boolean status;

    public static GradeVO getInstance(GradeEntity entity) {
        if (Objects.isNull(entity))
            return null;

         var probability = entity.getProbability();
        if (Objects.nonNull(probability)&&probability.compareTo(BigDecimal.ZERO)>0)
            probability = probability.multiply(new BigDecimal(1000));
        return GradeVO.builder()
                .id(entity.getId())
                .type(entity.getType())
                .sort(entity.getSort())
                .name(entity.getName())
                .probability(probability)
                .status(entity.getStatus())
                .build();
    }
}
