package com.lucky.domain.valueobject;

import com.lucky.domain.entity.PrizeInfoEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LogisticsOrderInfo {
    /**
     * 订单id
     */
    private Long id;
    /**
     * 订单状态
     */
    private Integer status;
    /**
     * 订单编号
     */
    private String number;

    /**
     * 快递单号
     */
    private String logisticsNumber;
    /**
     * 物流公司
     */
    private String logisticsCompany;
    /**
     * 订单创建时间
     */
    private LocalDateTime createTime;
    /**
     * 商品信息
     */
    private List<PrizeInfoEntity> goods;


}
