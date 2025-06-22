package com.lucky.domain.valueobject;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class LogisticsOrder {
    /**
     * 地址id
     */
    private Long addressId;
    /**
     * 商品
     */
    private List<PrizeInfoNum> goods;
    /**
     * 用户
     */
    private Long wechatUserId;
}
