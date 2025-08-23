package com.lucky.api.controller.admin.dto;

import com.lucky.domain.entity.SeriesTopicEntity;
import com.lucky.domain.exception.BusinessException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

/**
 * 主题 系列
 */
@Data

@AllArgsConstructor
@NoArgsConstructor
public class SeriesTopicDTO {
    /**
     * id
     */
    private Long id;
    /**
     * 名称
     */
    private String name;
    /**
     * 主题颜色
     */
    private String topicColor;
    /**
     * 主题图片
     */
    private String topicUrl;

    /**
     * 奖项
     */
    private List<Long> gradeIds;
    /**
     * 价格（多少钱一抽）
     */
    private BigDecimal price;
    /**
     * 是否启用
     */
    private Boolean status;
    /**
     * 排序
     */
    private Integer sort;

    public static SeriesTopicEntity toEntity(SeriesTopicDTO dto) {

        if (Objects.isNull(dto))
            throw BusinessException.newInstance("参数为空");

        return SeriesTopicEntity.builder()
                .id(dto.getId())
                .name(dto.getName())
                .topicColor(dto.getTopicColor())
                .status(dto.getStatus())
                .sort(dto.getSort())
                .topicUrl(dto.getTopicUrl())
                .price(dto.getPrice())
                .gradeIds(dto.getGradeIds())
                .build();

    }


}
