package com.lucky.domain;

import com.lucky.domain.entity.BannerEntity;
import com.lucky.domain.repository.BannerRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BannerService {
	private final BannerRepository bannerRepository;

	public BannerService(BannerRepository bannerRepository) {
		this.bannerRepository = bannerRepository;
	}

	public void saveOrUpdate(BannerEntity entity) {
		bannerRepository.saveOrUpdate(entity);
	}

	public List<BannerEntity> getBanner(BannerEntity entity) {
		return bannerRepository.getBanner(entity);
	}
	/**
	 * 删除
	 */
	public Boolean deleteById(Long id) {
		return bannerRepository.deleteById(id);
	}

}
