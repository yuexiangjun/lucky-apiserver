package com.lucky.domain.tripartite;

import com.lucky.domain.repository.tripartite.WechatRepository;

import com.lucky.domain.valueobject.*;
import org.springframework.stereotype.Component;

@Component
public class WechatService {
	private final WechatRepository wechatRepository;

	public WechatService(WechatRepository wechatRepository) {
		this.wechatRepository = wechatRepository;
	}

	/**
	 * 获取微信配置
	 *
	 * @return
	 */
	public WechatConfigValueObject getConfig() {
		return wechatRepository.getConfig();
	}


	/**
	 * 获取微信accessToken
	 *
	 * @return
	 */
	public String getAccessToken() {
		return wechatRepository.getAccessToken();
	}


	/**
	 * 根据获取手机号码的组件code获取手机号码
	 *
	 * @param code
	 * @return
	 */

	public WechatPhone getPhoneNumberToken(String code) {
		return wechatRepository.getPhoneNumberToken(code);
	}

	/**
	 * 小程序登录参数
	 */
	public Code2Session code2Session(String jsCode) {
		return wechatRepository.code2Session(jsCode);
	}

}
