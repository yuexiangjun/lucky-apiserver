package com.lucky.domain.entity;

import lombok.Data;

import java.time.LocalDateTime;


@Data
public class BannerEntity {
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
	/**
	 * 创建时间
	 */
	private LocalDateTime createTime;
	/**
	 * 修改时间
	 */
	private LocalDateTime updateTime;


}
