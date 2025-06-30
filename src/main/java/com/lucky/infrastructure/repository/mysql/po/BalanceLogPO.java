package com.lucky.infrastructure.repository.mysql.po;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.annotation.TableName;
import com.lucky.domain.entity.BalanceLogEntity;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@TableName("balance_log")
@Data
public class BalanceLogPO {
	/**
	 * id
	 */
	private Long id;
	/**
	 * 微信用户id
	 */
	private Long wechatUserId;
	/**
	 * 金额
	 */
	private BigDecimal money;
	/**
	 * 操作人id
	 */
	private Long operatorId;
	/**
	 * 操作时间
	 */
	private LocalDateTime operateTime;

	public static BalanceLogPO getInstance(BalanceLogEntity balanceLogEntity) {
		if (balanceLogEntity != null)
			return BeanUtil.toBean(balanceLogEntity, BalanceLogPO.class);
		return null;
	}
}
