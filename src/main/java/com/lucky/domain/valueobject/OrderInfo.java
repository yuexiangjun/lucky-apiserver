package com.lucky.domain.valueobject;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderInfo {
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
    private List<Order> orderList;

    /**
     * 支付方式
     */
    private Integer payType;



}
