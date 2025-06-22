package com.lucky.domain.valueobject;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PrizePublicity {
    /**
     * 中奖者姓名
     */
    private String name;
    /**
     * 中奖者头像
     */
    private String avatar;
    /**
     * 系列名称
     */
    private String seriesName;
    /**
     * 奖品名称
     */
    private String prizeName;
    /**
     * 中奖时间
     */
    private LocalDateTime time;
}
