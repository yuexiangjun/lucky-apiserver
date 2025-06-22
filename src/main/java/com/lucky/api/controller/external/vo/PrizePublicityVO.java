package com.lucky.api.controller.external.vo;

import cn.hutool.core.bean.BeanUtil;
import com.lucky.domain.valueobject.PrizePublicity;
import lombok.Data;

import java.util.Objects;

@Data
public class PrizePublicityVO {
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
    private String time;


    public static PrizePublicityVO toVO(PrizePublicity prizePublicity) {
        if (Objects.isNull(prizePublicity))
            return null;
        return BeanUtil.toBean(prizePublicity, PrizePublicityVO.class);
    }
}
