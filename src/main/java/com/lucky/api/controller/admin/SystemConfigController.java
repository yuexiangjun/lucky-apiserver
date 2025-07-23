package com.lucky.api.controller.admin;

import com.lucky.api.controller.admin.dto.SystemConfigDTO;
import com.lucky.api.controller.admin.vo.GradeVO;
import com.lucky.api.controller.admin.vo.SystemConfigVO;
import com.lucky.api.utils.ResponseFormat;
import com.lucky.application.SystemConfigServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 奖项最低限制金额
 *
 * @folder API/后台/奖项最低限制金额配置
 */
@Slf4j
@RestController
@RequestMapping("/api/system-config")
public class SystemConfigController {

	private final SystemConfigServer systemConfigServer;

	public SystemConfigController(SystemConfigServer systemConfigServer) {
		this.systemConfigServer = systemConfigServer;
	}

	/**
	 * 奖项的下拉框
	 */
	@GetMapping("/drop-down-box")
	@ResponseFormat
	public List<GradeVO> dropBox() {
		return systemConfigServer.findByList()
				.stream()
				.map(GradeVO::getInstance)
				.collect(Collectors.toList());
	}

	/**
	 * 修改添加
	 *
	 * @param dto
	 */
	@PostMapping
	@ResponseFormat
	public void saveOrUpdate(@RequestBody SystemConfigDTO dto) {
		systemConfigServer.saveOrUpdate(SystemConfigDTO.toEntity(dto));
	}

	/**
	 * 列表
	 *
	 * @return
	 */
	@GetMapping("/list")
	@ResponseFormat
	public List<SystemConfigVO> findAll() {
		return systemConfigServer.findAll()
				.stream()
				.map(SystemConfigVO::getInstance)
				.collect(Collectors.toList());

	}

	/**
	 * 删除
	 *
	 * @param id
	 */
	@DeleteMapping("/delete")
	@ResponseFormat
	public void deleteById(@RequestParam Long id) {
		systemConfigServer.deleteById(id);
	}

}