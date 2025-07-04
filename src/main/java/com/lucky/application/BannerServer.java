package com.lucky.application;

import com.lucky.domain.BannerService;
import com.lucky.domain.entity.BannerEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BannerServer {
	private final BannerService bannerService;

	public BannerServer(BannerService bannerService) {
		this.bannerService = bannerService;
	}

	public List<BannerEntity> getBanner(BannerEntity  entity) {
		return bannerService.getBanner(entity);
	}

	public Boolean deleteById(Long id) {
		return bannerService.deleteById(id);
	}

	public void saveOrUpdate(BannerEntity entity) {
		bannerService.saveOrUpdate(entity);
	}
}
