package com.lucky.domain.repository;

import com.lucky.domain.entity.BannerEntity;

import java.util.List;

public interface BannerRepository {

	void saveOrUpdate(BannerEntity entity);


	/**
	 * 获取Banner
	 */
	List<BannerEntity> getBanner(BannerEntity entity);

	Boolean deleteById(Long id);
}
