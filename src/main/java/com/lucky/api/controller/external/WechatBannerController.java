package com.lucky.api.controller.external;

import com.lucky.api.controller.admin.vo.BannerVO;
import com.lucky.api.utils.ResponseFormat;
import com.lucky.application.BannerServer;
import com.lucky.domain.entity.BannerEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;


/**
 * banner
 *
 * @folder API/小程序/banner
 */
@RestController

@RequestMapping("/api/wechat/banner")
public class WechatBannerController {
	private final BannerServer bannerServer;

	public WechatBannerController(BannerServer bannerServer) {
		this.bannerServer = bannerServer;
	}

	/**
	 * 列表
	 *
	 * @return
	 */
	@GetMapping("/list")
	@ResponseFormat
	public List<BannerVO> list() {
		var bannerEntity = new BannerEntity();
		bannerEntity.setEnabled(true);
		return bannerServer.getBanner(bannerEntity)
				.stream()
				.map(BannerVO::getInstance)
				.collect(Collectors.toList());
	}
}
