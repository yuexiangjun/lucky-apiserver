package com.lucky.domain.valueobject;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class PrizeInfoNum {
    /**
     * 商品id
     */
    private Long id;
    /**
     * 商品数量
     */
    private Integer num;
}
