package com.lucky.domain.valueobject;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ConsumeRank {
    /**
     * 用户图片
     */
    private String userImage;
    /**
     * 用户名称
     */
    private String userName;
    /**
     * 消费金额
     */
    private BigDecimal amount;
    /**
     * 订单数量
     */
    private Integer orderCount;
    /**
     * 归属客户
     */
    private String customer;
    /**
     * 加入时间
     */
    private LocalDateTime joinTime;
}
