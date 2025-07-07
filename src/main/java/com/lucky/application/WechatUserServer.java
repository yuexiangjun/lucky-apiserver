package com.lucky.application;


import cn.hutool.core.collection.CollectionUtil;
import com.lucky.application.interceptor.JwtUtils;
import com.lucky.application.interceptor.LoginUserEntity;
import com.lucky.application.interceptor.TokenEntity;
import com.lucky.domain.PayOrderService;
import com.lucky.domain.WechatUserService;
import com.lucky.domain.entity.PayOrderEntity;
import com.lucky.domain.entity.WechatUserEntity;
import com.lucky.domain.exception.BusinessException;
import com.lucky.domain.valueobject.WechatUserList;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class WechatUserServer {
	private final WechatUserService wechatUserService;
	private final PayOrderService payOrderService;

	public WechatUserServer(WechatUserService wechatUserService, PayOrderService payOrderService) {
		this.wechatUserService = wechatUserService;
		this.payOrderService = payOrderService;
	}


	/**
	 * 登录
	 */
	public LoginUserEntity login(WechatUserEntity entity) {
		if (entity.getOpenid() == null) {
			throw BusinessException.newInstance("手机号或openid能为空");
		}
		var oldEntity = wechatUserService.getByOpenId(entity.getOpenid());

		if (Objects.isNull(oldEntity)) {
			entity.setEnabled(true);
			entity.setCreateTime(LocalDateTime.now());
			oldEntity = entity;
		} else {
			if (Objects.equals(entity.getEnabled(), 0)) {
				throw BusinessException.newInstance("账号已被禁用");
			}
		}

		oldEntity.setLastLoginTime(LocalDateTime.now());

		var id = wechatUserService.saveOrUpdate(oldEntity);

		var tokenEntity = TokenEntity.builder()
				.userId(String.valueOf(id))
				.username(oldEntity.getOpenid())
				.client(2)
				.createTime(String.valueOf(System.currentTimeMillis()))
				.build();

		var token = JwtUtils.createToken(tokenEntity);

		return LoginUserEntity.builder()
				.authorization(token)
				.username(oldEntity.getName())
				.phone(oldEntity.getPhone())
				.id(oldEntity.getId())
				.build();
	}

	/**
	 * 更据openid查询
	 */
	public WechatUserEntity getByOpenId(String openId) {
		if (Strings.isBlank(openId))
			return null;
		return wechatUserService.getByOpenId(openId);
	}

	/**
	 * 添加/修改用户
	 */
	public Long saveOrUpdate(WechatUserEntity wechatUserEntity) {
		return wechatUserService.saveOrUpdate(wechatUserEntity);
	}

	public WechatUserEntity getInfo(Long wechatUserId) {
		return wechatUserService.getById(wechatUserId);
	}


	/**
	 * 用户列表
	 */
	public List<WechatUserList> list(WechatUserEntity wechatUserEntity, String ownerName) {

		if (Strings.isNotBlank(ownerName)) {

			var wechatUserEntity1 = wechatUserService.getWechatUserEntity(WechatUserEntity.builder().name(ownerName).build());

			if (Objects.nonNull(wechatUserEntity1))
				wechatUserEntity.setOwnerId(wechatUserEntity1.getId());
		}

		var wechatUserEntities = wechatUserService.list(wechatUserEntity);

		if (CollectionUtil.isEmpty(wechatUserEntities))
			return List.of();

		//获取定单数
		var wechatUserIds = wechatUserEntities
				.stream()
				.map(WechatUserEntity::getId)
				.collect(Collectors.toList());

		var byWechatUserIds = payOrderService.findByWechatUserIds(wechatUserIds)
				.stream()
				.collect(Collectors.groupingBy(PayOrderEntity::getWechatUserId));


		var ownerIds = wechatUserEntities.stream()
				.map(WechatUserEntity::getOwnerId)
				.collect(Collectors.toList());

		var ownerMap = wechatUserService.getByIds(ownerIds)
				.stream()
				.collect(Collectors.toMap(WechatUserEntity::getId, Function.identity()));


		return wechatUserEntities
				.stream()
				.map(s -> {
					var wechatUserList = WechatUserList.getInstance(s);


					var orDefault = byWechatUserIds.getOrDefault(s.getId(), List.of());
					wechatUserList.setOrderCount(orDefault.size());
					wechatUserList.setAmountSpent(orDefault.stream().map(PayOrderEntity::getPayMoney).reduce(BigDecimal.ZERO, BigDecimal::add));
					if (Objects.nonNull(s.getOwnerId())) {
						WechatUserEntity wechatUser = ownerMap.getOrDefault(s.getOwnerId(), new WechatUserEntity());
						wechatUserList.setOwnerName(Objects.isNull(wechatUser.getName()) ? wechatUser.getPhone() : wechatUser.getName());

					}
					return wechatUserList;

				}).collect(Collectors.toList());


	}

	/**
	 * 增加用户余额
	 */
	public void balanceAdd(Long wechatUserId, BigDecimal money, Long userId) {
		wechatUserService.balanceAdd(wechatUserId, money, userId);
	}

	/**
	 * 更据手机号码获取用户
	 */
	public WechatUserEntity getByPhone(String phone) {
		if (Strings.isBlank(phone))
			return null;
		return wechatUserService.getByPhone(phone);
	}
}
