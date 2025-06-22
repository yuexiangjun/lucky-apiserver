package com.lucky.api.controller.admin.dto;

import com.lucky.domain.entity.GradeEntity;
import com.lucky.domain.exception.BusinessException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * 奖项
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GradeDTO {
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
     * 名称
     */
    private String name;
    /**
     * 排序
     */
    private Integer sort;
    /**
     * 概率 隐藏类型的必传 普通级别的不用传
     */
    private BigDecimal probability;
    /**
     * 是否启用
     */
    private Boolean status;

    public static GradeEntity toEntity(GradeDTO dto) {
        if (Objects.isNull(dto))
            throw BusinessException.newInstance("参数为空");
        var probability = dto.getProbability();
        if (Objects.nonNull(probability) && probability.compareTo(BigDecimal.ZERO) > 0)
            probability = probability.divide(new BigDecimal(1000));
        return GradeEntity.builder()
                .id(dto.getId())
                .type(dto.getType())
                .sort(dto.getSort())
                .name(dto.getName())
                .probability(probability)
                .status(dto.getStatus())
                .build();
    }
}
