package com.lucky.api.controller.external.dto;

import com.lucky.domain.entity.WechatUserEntity;
import com.lucky.domain.exception.BusinessException;
import lombok.Data;

import java.util.Objects;

@Data
public class WechatUserInfoDTO {
	/**
	 * 用户昵称
	 */
	private String nickName;
	/**
	 * 微信头像
	 */
	private String avatarUrl;
	/**
	 * openId
	 */
	private String openId;

	/**
	 * 不加区号的手机号码
	 */
	private String purePhoneNumber;
	public static WechatUserEntity toEntity(WechatUserInfoDTO dto) {
		if (Objects.isNull(dto))
			throw BusinessException.newInstance("参数缺失");
		return WechatUserEntity.builder()
				.name(dto.getNickName())
				.avatar(dto.getAvatarUrl())
				.openid(dto.getOpenId())
				.phone(dto.getPurePhoneNumber())
				.build();


	}

}
