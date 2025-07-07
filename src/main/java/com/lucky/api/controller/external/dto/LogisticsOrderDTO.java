package com.lucky.api.controller.external.dto;

import com.lucky.domain.valueobject.LogisticsOrder;
import com.lucky.domain.valueobject.PrizeInfoNum;
import lombok.Data;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class LogisticsOrderDTO {
    /**
     * 地址id
     */
    private Long addressId;
    /**
     * 商品
     */
    private List<PrizeInfoNumDTO> goods;
    /**
     * 用户
     */
    private Long wechatUserId;

    /**
     * 快递费
     */
    private BigDecimal expressMoney;

    /**
     *
     * @param dto
     * @return
     */

     public  static LogisticsOrder toLogisticsOrder(LogisticsOrderDTO dto) {
        if (CollectionUtils.isEmpty(dto.getGoods()))
            throw new RuntimeException("商品不能为空");

        var prizeInfos = dto.getGoods()
                .stream()
                .map(s -> PrizeInfoNum.builder().num(s.getNum()).id(s.getId()).build())
                .collect(Collectors.toList());
        return LogisticsOrder.builder()
                .wechatUserId(dto.getWechatUserId())
                .addressId(dto.getAddressId())
                .expressMoney(dto.getExpressMoney())
                .goods(prizeInfos)
                .build();

    }
}
