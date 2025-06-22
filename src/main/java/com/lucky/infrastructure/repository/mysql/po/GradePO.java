package com.lucky.infrastructure.repository.mysql.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
@TableName(value = "grade")
public class GradePO {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
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
     * 概率
     */
    private BigDecimal probability;
    /**
     * 是否启用
     */
    private Boolean status;

    public static GradePO getInstance(GradeEntity entity) {
        if (Objects.isNull(entity))
            return null;
        return GradePO.builder()
                .id(entity.getId())
                .type(entity.getType())
                .sort(entity.getSort())
                .name(entity.getName())
                .probability(entity.getProbability())
                .status(entity.getStatus())

                .build();

    }

    public static GradeEntity toEntity(GradePO po) {
        if (Objects.isNull(po))
            return null;
        return GradeEntity.builder()
                .id(po.getId())
                .sort(po.getSort())
                .type(po.getType())
                .name(po.getName())
                .probability(po.getProbability())
                .status(po.getStatus())
                .build();

    }
}
