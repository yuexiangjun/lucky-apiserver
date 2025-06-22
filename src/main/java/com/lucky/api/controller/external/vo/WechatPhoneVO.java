package com.lucky.api.controller.external.vo;
import cn.hutool.core.bean.BeanUtil;
import com.lucky.domain.valueobject.WechatPhone;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class WechatPhoneVO {
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

	public static WechatPhoneVO getInstance(WechatPhone wechatPhone) {
		if (wechatPhone == null)
			return null;
		return BeanUtil.toBean(wechatPhone, WechatPhoneVO.class);

	}


}