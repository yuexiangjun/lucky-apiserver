package com.lucky.api.controller.external.dto;

import lombok.Data;

@Data
public class PrizePublicityDTO {
    /**
     *系列id
     */
    private Long topicId;
    /**
     * 奖品等级类型 1-特殊奖品 2-普通奖品
     */
    private Integer gradeType;
}
