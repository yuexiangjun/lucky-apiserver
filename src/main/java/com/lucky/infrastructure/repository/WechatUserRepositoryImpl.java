package com.lucky.infrastructure.repository;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lucky.domain.entity.WechatUserEntity;
import com.lucky.domain.repository.WechatUserRepository;
import com.lucky.infrastructure.repository.mysql.mapper.WechatUserMapper;
import com.lucky.infrastructure.repository.mysql.po.WechatUserPO;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class WechatUserRepositoryImpl extends ServiceImpl<WechatUserMapper, WechatUserPO> implements WechatUserRepository {

	@Override
	public WechatUserEntity getByPhone(String phone) {

		var wrapper = Wrappers.lambdaQuery(WechatUserPO.class)
				.eq(WechatUserPO::getPhone, phone);

		var one = this.getOne(wrapper);

		return WechatUserPO.toEntity(one);

	}

	@Override
	public Long saveOrUpdate(WechatUserEntity entity) {

		var po = WechatUserPO.getInstance(entity);

		if (Objects.isNull(po))
			return null;

		this.saveOrUpdate(po);

		return po.getId();
	}

	@Override
	public List<WechatUserEntity> list(WechatUserEntity entity) {

		var wrapper = Wrappers.lambdaQuery(WechatUserPO.class)
				.eq(Strings.isNotBlank(entity.getPhone()), WechatUserPO::getPhone, entity.getPhone())
				.eq(Strings.isNotBlank(entity.getOpenid()), WechatUserPO::getOpenid, entity.getOpenid())
				.eq(Strings.isNotBlank(entity.getName()), WechatUserPO::getName, entity.getName());

		return this.list(wrapper)
				.stream()
				.map(WechatUserPO::toEntity)
				.collect(Collectors.toList());

	}

	@Override
	public WechatUserEntity getById(Long id) {
		var wrapper = Wrappers.lambdaQuery(WechatUserPO.class)
				.eq(WechatUserPO::getId, id);
		var one = this.getOne(wrapper);

		return WechatUserPO.toEntity(one);
	}

	@Override
	public WechatUserEntity getByOpenId(String openId) {
		var wrapper = Wrappers.lambdaQuery(WechatUserPO.class)
				.eq(WechatUserPO::getOpenid, openId);

		var one = this.getOne(wrapper);

		return WechatUserPO.toEntity(one);
	}

	@Override
	public List<WechatUserEntity> getByIds(List<Long> wechatUserIds) {
		if (CollectionUtils.isEmpty(wechatUserIds))
			return List.of();
		var wrapper = Wrappers.lambdaQuery(WechatUserPO.class)
				.in(WechatUserPO::getId, wechatUserIds);
		return this.list(wrapper)
				.stream()
				.map(WechatUserPO::toEntity)
				.collect(Collectors.toList());
	}
}
