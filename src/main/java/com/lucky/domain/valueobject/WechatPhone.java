package com.lucky.domain.valueobject;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class WechatPhone {
	/**
	 * 加区号的手机号码
	 */
	private String phoneNumber;
	/**
	 * 不加区号的手机号码
	 */
	private String purePhoneNumber;
	/**
	 * 区号
	 */
	private String countryCode;


}
