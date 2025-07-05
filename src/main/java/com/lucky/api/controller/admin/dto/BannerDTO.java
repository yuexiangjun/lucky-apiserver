package com.lucky.api.controller.admin.dto;

import cn.hutool.core.bean.BeanUtil;
import com.lucky.domain.entity.BannerEntity;
import lombok.Data;


@Data
public class BannerDTO {
	/**
	 * id
	 */
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
	 * 排序
	 */
	private  Integer sort;
	/**
	 * 启用禁用
	 */
	private Boolean enabled;

	public static BannerEntity toEntity(BannerDTO dto) {

		if (dto == null)
			return null;
		return BeanUtil.toBean(dto, BannerEntity.class);

	}

}
