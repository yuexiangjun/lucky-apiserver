package com.lucky.infrastructure.repository.mysql.po;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.lucky.domain.entity.WechatUserEntity;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("wechat_user")
public class WechatUserPO {
	/**
	 * id
	 */
	@TableId(value = "id", type = IdType.ASSIGN_ID)
	private Long id;
	/**
	 * 头像地址
	 */
	private String avatar;
	/**
	 * openid
	 */
	private String openid;
	/**
	 * 姓名
	 */
	private String name;
	/**
	 * 手机号码
	 */
	private String phone;

	/**
	 * 是否启用
	 */
	private  Boolean enabled;
	/**
	 * 最后一次登录时间
	 */
	private LocalDateTime lastLoginTime;
	/**
	 * 账户余额
	 */
	private BigDecimal balance;
	/**
	 * 创建时间
	 */
	private LocalDateTime createTime;

	public static WechatUserPO getInstance(WechatUserEntity entity) {
		if (Objects.isNull(entity))
			return null;
		return BeanUtil.toBean(entity, WechatUserPO.class);
	}

	public static WechatUserEntity toEntity(WechatUserPO po) {
		if (Objects.isNull(po))
			return null;
		return BeanUtil.toBean(po, WechatUserEntity.class);
	}
}
