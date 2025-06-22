package com.lucky.api.controller.external.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Code2SessionVO {

	/**
	 * 微信用户id
	 */
	private Long wechatUserId;

	/**
	 * token
	 */
	private String authorization;

}
