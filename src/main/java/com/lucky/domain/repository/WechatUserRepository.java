package com.lucky.domain.repository;

import com.lucky.domain.entity.WechatUserEntity;

import java.time.LocalDateTime;
import java.util.List;

public interface WechatUserRepository  {
	WechatUserEntity getByPhone(String phone);

	Long saveOrUpdate(WechatUserEntity wechatUserEntity);

	List<WechatUserEntity> list(WechatUserEntity wechatUserEntity);

	WechatUserEntity getById(Long id);

	WechatUserEntity getByOpenId(String openId);

	List<WechatUserEntity> getByIds(List<Long> wechatUserIds);

	WechatUserEntity getWechatUserEntity(WechatUserEntity wechatUserEntity);

	List<WechatUserEntity> listByTime(WechatUserEntity wechatUserEntity, LocalDateTime startTime, LocalDateTime endTime);
}
