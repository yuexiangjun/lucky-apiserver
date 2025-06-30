package com.lucky.domain.valueobject;

import cn.hutool.core.bean.BeanUtil;
import com.lucky.domain.entity.WechatUserEntity;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Data
public class WechatUserList {
	/**
	 * 微信用户ID
	 */
	private Long id;
	/**
	 * 姓名
	 */
	private String name;
	/**
	 * 手机号
	 */
	private String phone;
	/**
	 * 消费金额
	 */
	private BigDecimal amountSpent;
	/**
	 * 点单数
	 */
	private Integer orderCount;
	/**
	 * 账户余额
	 */
	private BigDecimal balance;
	/**
	 * 归属用户
	 */
	private String ownerName;
	/**
	 * 创建时间
	 */
	private LocalDateTime createTime;
	/**
	 * 最后登录时间
	 */
	private LocalDateTime lastLoginTime;

	public static  WechatUserList getInstance(WechatUserEntity entity){
		if(Objects.isNull(entity))
			return null;
		return BeanUtil.toBean(entity, WechatUserList.class);

	}

}
