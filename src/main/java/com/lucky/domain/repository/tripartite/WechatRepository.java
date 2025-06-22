package com.lucky.domain.repository.tripartite;


import com.lucky.domain.valueobject.Code2Session;
import com.lucky.domain.valueobject.WechatConfigValueObject;
import com.lucky.domain.valueobject.WechatPhone;

public interface WechatRepository {
	/**
	 * 获取微信配置
	 *
	 * @return
	 */
	WechatConfigValueObject getConfig();

	/**
	 * 获取微信accessToken
	 *
	 * @return
	 */
	String getAccessToken();

	/**
	 * 根据获取手机号码的组件code获取手机号码
	 *
	 * @param code
	 * @return
	 */

	WechatPhone getPhoneNumberToken(String code);

	/**
	 * 小程序登录参数
	 */
	 Code2Session code2Session(String jsCode);
}
