package com.lucky.api.controller.admin.vo;

import cn.hutool.core.bean.BeanUtil;
import com.lucky.domain.valueobject.WechatUserList;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Data
public class WechatUserListVO {
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

	public static WechatUserListVO getInstance(WechatUserList entity) {
		if (Objects.isNull(entity))
			return null;
		return BeanUtil.toBean(entity, WechatUserListVO.class);

	}

}
