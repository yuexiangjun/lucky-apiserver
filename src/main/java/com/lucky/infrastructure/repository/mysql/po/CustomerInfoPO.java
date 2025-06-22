package com.lucky.infrastructure.repository.mysql.po;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.lucky.domain.entity.CustomerInfoEntity;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 客服信息
 */
@Data
@TableName("customer_info")
public class CustomerInfoPO {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;
    /**
     * 客服名称
     */
    private String name;
    /**
     * 客服电话
     */
    private String phone;
    /**
     * 客服微信
     */
    private String wechat;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    public static CustomerInfoPO getInstance(CustomerInfoEntity entity) {
        if (Objects.isNull(entity))
            return null;
        return BeanUtil.toBean(entity, CustomerInfoPO.class);
    }

    public static CustomerInfoEntity toEntity(CustomerInfoPO po) {
        if (Objects.isNull(po))
            return null;
        return BeanUtil.toBean(po, CustomerInfoEntity.class);
    }

}
