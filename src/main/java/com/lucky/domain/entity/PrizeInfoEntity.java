package com.lucky.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 奖品
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PrizeInfoEntity {
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
}
