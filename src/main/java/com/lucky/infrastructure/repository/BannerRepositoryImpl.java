package com.lucky.infrastructure.repository;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lucky.domain.entity.BannerEntity;
import com.lucky.domain.repository.BannerRepository;
import com.lucky.infrastructure.repository.mysql.mapper.BannerMapper;
import com.lucky.infrastructure.repository.mysql.po.BannerPO;
import com.lucky.infrastructure.repository.mysql.po.CustomerInfoPO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class BannerRepositoryImpl extends ServiceImpl<BannerMapper, BannerPO> implements BannerRepository {
	public void saveOrUpdate(BannerEntity entity) {
		var bannerPO = BannerPO.getInstance(entity);

		this.saveOrUpdate(bannerPO);

	}

	/**
	 * 获取Banner
	 */
	public List<BannerEntity> getBanner(BannerEntity entity) {

		var  wrapper= Wrappers.lambdaQuery(BannerPO.class)
				.eq(Objects.nonNull(entity.getEnabled()),BannerPO::getEnabled, entity.getEnabled())
				.orderByAsc(BannerPO::getSort)
				.orderByDesc(BannerPO::getCreateTime);

		return this.list(wrapper)
				.stream()
				.map(BannerPO::toEntity)
				.collect(Collectors.toList());
	}

	@Override
	public Boolean deleteById(Long id) {

		int i = this.baseMapper.deleteById(id);

		return Objects.nonNull(i) && i > 0;
	}

}
