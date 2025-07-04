package com.lucky.api.controller.admin;

import com.lucky.api.controller.admin.dto.BannerDTO;
import com.lucky.api.controller.admin.vo.BannerVO;
import com.lucky.application.BannerServer;
import com.lucky.domain.entity.BannerEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
/**
 * banner
 *
 * @folder API/后台/banner
 */
@RestController

@RequestMapping("/api/admin/banner")
public class BannerController {
	private final BannerServer bannerServer;

	public BannerController(BannerServer bannerServer) {
		this.bannerServer = bannerServer;
	}

	/**
	 * 添加修改
	 * @param dto
	 */
	@PostMapping("/save")
	public void save(@RequestBody BannerDTO dto) {
		bannerServer.saveOrUpdate(BannerDTO.toEntity(dto));

	}

	/**
	 * 删除
	 * @param id
	 */
	@DeleteMapping("/deleteById")
	public void deleteById(@RequestParam Long id) {
		bannerServer.deleteById(id);
	}

	/**
	 * 列表
	 * @return
	 */
	@GetMapping("/list")
	public List<BannerVO> list() {
		 var bannerEntity = new BannerEntity();
		return bannerServer.getBanner(bannerEntity)
				.stream()
				.map(BannerVO::getInstance)
				.collect(Collectors.toList());
	}
}
