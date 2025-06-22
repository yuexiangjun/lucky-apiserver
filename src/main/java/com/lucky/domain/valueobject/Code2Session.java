package com.lucky.domain.valueobject;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Code2Session {
	private String openid;
	private String sessionKey;

	/**
	 * 微信用户id
	 */
	private Long wechatUserId;
	/**
	 * Authorization
	 */
	private String authorization;
}
