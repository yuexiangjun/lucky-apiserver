package com.lucky.domain.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 客服信息
 */
@Data
public class CustomerInfoEntity {
    /**
     * id
     */
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


}
