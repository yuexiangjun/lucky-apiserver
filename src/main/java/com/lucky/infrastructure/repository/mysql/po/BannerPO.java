package com.lucky.infrastructure.repository.mysql.po;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.lucky.domain.entity.BannerEntity;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Objects;

@TableName("banner")
@Data
public class BannerPO {
	/**
	 * id
	 */
	@TableId(value = "id", type = IdType.ASSIGN_ID)
	private Long id;
	/**
	 * banner名称
	 */
	private String name;
	/**
	 * banner图片
	 */
	private String image;
	/**
	 * 启用禁用
	 */
	private Boolean enabled;
	/**
	 * 创建时间
	 */
	private LocalDateTime createTime;
	/**
	 * 修改时间
	 */
	private LocalDateTime updateTime;

	public static BannerPO getInstance(BannerEntity entity) {
		if (Objects.isNull(entity))
			return null;
		var po = BeanUtil.toBean(entity, BannerPO.class);
		if (Objects.isNull(entity.getId()))
			po.setCreateTime(LocalDateTime.now());
		else
			po.setUpdateTime(LocalDateTime.now());
		return po;
	}


	public static BannerEntity toEntity(BannerPO po) {
		if (Objects.isNull(po))
			return null;
		return BeanUtil.toBean(po, BannerEntity.class);

	}


}
