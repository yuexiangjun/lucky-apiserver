package com.lucky.domain;

import cn.hutool.core.collection.CollectionUtil;
import com.lucky.domain.entity.BalanceLogEntity;
import com.lucky.domain.entity.WechatUserEntity;
import com.lucky.domain.exception.BusinessException;
import com.lucky.domain.repository.BalanceLogRepository;
import com.lucky.domain.repository.WechatUserRepository;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Component

public class WechatUserService {
	private final WechatUserRepository wechatUserRepository;

	private final BalanceLogRepository balanceLogRepository;


	public WechatUserService(WechatUserRepository wechatUserRepository, BalanceLogRepository balanceLogRepository) {
		this.wechatUserRepository = wechatUserRepository;

		this.balanceLogRepository = balanceLogRepository;
	}

	/**
	 * 更据手机号码获取用户
	 */
	public WechatUserEntity getByPhone(String phone) {
		if (Strings.isBlank(phone))
			return null;
		return wechatUserRepository.getByPhone(phone);
	}

	/**
	 * 添加/修改用户
	 */
	public Long saveOrUpdate(WechatUserEntity wechatUserEntity) {
		return wechatUserRepository.saveOrUpdate(wechatUserEntity);
	}

	/**
	 * 列表
	 */
	public List<WechatUserEntity> list(WechatUserEntity wechatUserEntity) {
		return wechatUserRepository.list(wechatUserEntity);
	}

	/**
	 * 更据id查询
	 */
	public WechatUserEntity getById(Long id) {
		if (Objects.isNull(id))
			return null;
		return wechatUserRepository.getById(id);
	}

	/**
	 * 更据openid查询
	 */
	public WechatUserEntity getByOpenId(String openId) {
		if (Strings.isBlank(openId))
			return null;
		return wechatUserRepository.getByOpenId(openId);
	}


	public List<WechatUserEntity> getByIds(List<Long> wechatUserIds) {
		return wechatUserRepository.getByIds(wechatUserIds);
	}

	/**
	 * 扣减余额
	 */
	public void balanceReduce(Long wechatUserId, BigDecimal money, String remark) {

		var wechatUserEntity = wechatUserRepository.getById(wechatUserId);
		if (Objects.isNull(wechatUserEntity))
			throw BusinessException.newInstance("用户不存在");

		if (Objects.isNull(wechatUserEntity.getBalance()))
			throw BusinessException.newInstance("用户积分余额不足");

		if (wechatUserEntity.getBalance().compareTo(money) < 0)
			throw BusinessException.newInstance("用户积分余额不足");

		wechatUserEntity.setBalance(wechatUserEntity.getBalance().subtract(money));

		wechatUserRepository.saveOrUpdate(wechatUserEntity);
	}

	public List<WechatUserEntity> listByTime(WechatUserEntity wechatUserEntity, LocalDateTime startTime, LocalDateTime endTime) {
		return wechatUserRepository.listByTime(wechatUserEntity, startTime, endTime);
	}

	/**
	 * 添加余额
	 */
	@Transactional
	public void balanceAdd(Long wechatUserId, BigDecimal money, Long userId) {
		var wechatUserEntity = wechatUserRepository.getById(wechatUserId);

		if (Objects.isNull(wechatUserEntity))
			throw BusinessException.newInstance("用户不存在");

		wechatUserEntity.setBalance(Objects.isNull(wechatUserEntity.getBalance())?BigDecimal.ZERO.add(money):wechatUserEntity.getBalance().add(money));

		wechatUserRepository.saveOrUpdate(wechatUserEntity);

		var balanceLogEntity = BalanceLogEntity
				.builder()
				.wechatUserId(wechatUserId)
				.money(money)
				.operateTime(LocalDateTime.now())
				.operatorId(userId)
				.build();

		balanceLogRepository.saveOrUpdate(balanceLogEntity);
	}


	public WechatUserEntity getWechatUserEntity(WechatUserEntity build) {
		return wechatUserRepository.getWechatUserEntity(build);
	}

	public List<WechatUserEntity> getUserNameOrPhone(String userNameOrPhone) {
		var userNameOrPhone1 = wechatUserRepository.getUserNameOrPhone(userNameOrPhone);
		if (Strings.isNotBlank(userNameOrPhone) && CollectionUtil.isEmpty(userNameOrPhone1)) {

			var entity = new WechatUserEntity();
			entity.setId(-1l);
			return List.of(entity);
		}
		return userNameOrPhone1;

	}

	public Integer count() {
		return wechatUserRepository.wechatcount();

	}
}
