package com.lucky.api.controller.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class PageSizeDTO {
	/**
	 * 页码
	 */
	private Integer page;
	/**
	 * 每页数量
	 */
	private Integer size;
}
