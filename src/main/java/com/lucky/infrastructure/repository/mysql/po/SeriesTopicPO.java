package com.lucky.infrastructure.repository.mysql.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.lucky.domain.entity.SeriesTopicEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

/**
 * 主题 系列
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "series_topic",autoResultMap = true)
public class SeriesTopicPO {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private  Long id;
    /**
     * 名称
     */
    private String name;
    /**
     * 主题颜色
     */
    private String topicColor;
    /**
     * 场次(一共多少场)
     *
     */
    private Integer session;
    /**
     * 主题图片
     */
    private String topicUrl;
    /**
     * 是否启用
     */
    private  Boolean status;
    /**
     * 价格（多少钱一抽）
     */
    private BigDecimal price;
    /**
     * 奖项
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<Long> gradeId;

    /**
     * 排序
     */
    private Integer sort;

    /**
     *
     * @param entity
     * @return
     */

    public static SeriesTopicPO getInstance(SeriesTopicEntity entity) {
        if (Objects.isNull(entity))
            return null;
        return SeriesTopicPO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .price(entity.getPrice())
                .topicColor(entity.getTopicColor())
                .session(entity.getSession())
                .status(entity.getStatus())
                .sort(entity.getSort())
                .topicUrl(entity.getTopicUrl())
                .gradeId(entity.getGradeIds())
                .build();

    }

    public static SeriesTopicEntity toEntity(SeriesTopicPO po) {
        if (Objects.isNull(po))
            return null;
        return SeriesTopicEntity.builder()
                .id(po.getId())
                .name(po.getName())
                .price(po.getPrice())
                .topicColor(po.getTopicColor())
                .sort(po.getSort())
                .session(po.getSession())
                .status(po.getStatus())
                .topicUrl(po.getTopicUrl())
                .gradeIds(po.getGradeId())
                .build();

    }



}
