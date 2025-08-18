package com.lucky.api.controller.admin.vo;

import cn.hutool.core.bean.BeanUtil;
import com.lucky.domain.valueobject.ConsumeRank;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Data
public class ConsumeRankVO {
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
    /**
     * 微信消费
     */
    private BigDecimal wechatConsume;
    /**
     * 福币消费
     */
    private BigDecimal coinConsume;



    public static ConsumeRankVO getInstance(ConsumeRank consumeRank){
        if (Objects.isNull(consumeRank))
            return null;
      return BeanUtil.toBean(consumeRank, ConsumeRankVO.class);


    }
}

