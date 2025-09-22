package com.lucky.api.controller.admin.vo;

import cn.hutool.core.bean.BeanUtil;
import com.lucky.domain.valueobject.Order;
import com.lucky.domain.valueobject.OrderInfo;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderInfoVO {
    /**
     * id
     *
     */
    private Long id;

    /**
     * 微信用户id
     */
    private Long wechatUserId;
    /**
     * 微信用户名字
     */
    private String wechatName;
    /**
     * 主题id
     */
    private Long topicId;
    /**
     * 系列名
     */
    private String topicName;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 商品详情
     */
    private List<OrderVO> orderList;

    /**
     * 支付方式
     */
    private Integer payType;


    public static OrderInfoVO getInstance(OrderInfo orderInfo) {
        if (Objects.isNull(orderInfo))
        return null;

        OrderInfoVO bean = BeanUtil.toBean(orderInfo, OrderInfoVO.class);

        List<OrderVO> collect = orderInfo.getOrderList()
                .stream()
                .map(OrderVO::getInstance)
                .collect(Collectors.toList());

        bean.setOrderList(collect);
        return bean;
    }
}
