package com.lucky.api.controller.external.dto;

import cn.hutool.core.bean.BeanUtil;
import com.lucky.domain.entity.PayOrderEntity;
import com.lucky.domain.exception.BusinessException;
import lombok.Data;

import java.util.Objects;

@Data
public class PayDTO {
    /**
     * 支付人
     */
    private Long wechatUserId;
    /**
     * 主题id
     */
    private Long topicId;
    /**
     * 场次id
     */
    private Long sessionId;
    /**
     * 抽奖次数
     */
    private Integer times;



    public static PayOrderEntity toTripartiteEntity(PayDTO dto) {
        if (Objects.isNull(dto))
            throw BusinessException.newInstance("缺少参数");
         var bean = BeanUtil.toBean(dto, PayOrderEntity.class);
        bean.setPayType(1);
        bean.setOrderType(1);
        return bean;


    }


    public static PayOrderEntity toBalanceEntity(PayDTO dto) {
        if (Objects.isNull(dto))
            throw BusinessException.newInstance("缺少参数");
        var bean = BeanUtil.toBean(dto, PayOrderEntity.class);
        bean.setPayType(2);
        bean.setOrderType(1);
        return bean;

    }
}
