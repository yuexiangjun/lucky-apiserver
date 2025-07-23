package com.lucky.infrastructure.repository.mysql.po;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.lucky.domain.entity.SystemConfigEntity;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Objects;

@TableName(value = "system_config")
@Data
public class SystemConfigPO {
	/**
	 * 系统配置id
	 */
	@TableId(value = "id", type = IdType.ASSIGN_ID)
	private Long id;
	/**
	 * 奖品等级id
	 */
	private Long gradeId;
	/**
	 * 最低消费金额
	 */
	private BigDecimal minConsume;


	public static SystemConfigPO getInstance(SystemConfigEntity entity) {
		if (Objects.isNull(entity))
			return null;
		return BeanUtil.toBean(entity, SystemConfigPO.class);
	}

	public static SystemConfigEntity toEntity(SystemConfigPO po) {
		if (Objects.isNull(po))
			return null;
		return BeanUtil.toBean(po, SystemConfigEntity.class);
	}

}
