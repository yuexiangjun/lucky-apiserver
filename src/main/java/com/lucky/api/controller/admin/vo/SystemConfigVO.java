package com.lucky.api.controller.admin.vo;

import cn.hutool.core.bean.BeanUtil;
import com.lucky.domain.entity.SystemConfigEntity;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Objects;

@Data
public class SystemConfigVO {
	/**
	 * 系统配置id
	 */

	private Long id;
	/**
	 * 奖品等级id
	 */
	private Long gradeId;
	/**
	 * 奖项名称
	 */
	private String gradeName;
	/**
	 * 最低消费金额
	 */
	private BigDecimal minConsume;

	public static SystemConfigVO getInstance(SystemConfigEntity entity) {
		if (Objects.isNull(entity))
			return null;
		return BeanUtil.toBean(entity, SystemConfigVO.class);
	}
}