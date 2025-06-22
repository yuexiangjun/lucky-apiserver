package com.lucky.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * 主题 系列
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SeriesTopicEntity {
    /**
     * id
     */
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
     * 奖项
     */
    private List<Long> gradeIds;
    /**
     * 价格（多少钱一抽）
     */
    private BigDecimal price;




}
