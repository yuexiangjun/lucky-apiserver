package com.lucky.infrastructure.repository.mysql.po;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.lucky.domain.entity.NoCounterEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * 公共表 - CRM-编号计数器
 *
 * @TableName no_counter
 */
@TableName(value = "no_counter")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NoCounterPO {
	/**
	 * id
	 */
	@TableId(value = "id", type = IdType.ASSIGN_ID)
	private Long id;

	/**
	 * 编号类型
	 */
	@TableField(value = "type")
	private String type;

	/**
	 * 数值
	 */
	@TableField(value = "value")
	private Integer value;

	/**
	 * 所属天
	 */
	@TableField(value = "belong_day")
	private LocalDate belongDay;

	public static  NoCounterPO getInstance(NoCounterEntity  entity){
		if (entity == null)
			return null;
		return BeanUtil.toBean(entity,NoCounterPO.class);
	}

	public static  NoCounterEntity toEntity(NoCounterPO  po){
		if (po == null)
			return null;
		return BeanUtil.toBean(po,NoCounterEntity.class);
	}

}