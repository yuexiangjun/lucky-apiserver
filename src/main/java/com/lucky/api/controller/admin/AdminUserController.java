package com.lucky.api.controller.admin;

import com.lucky.api.controller.admin.dto.*;
import com.lucky.api.controller.admin.vo.AdminUserVO;
import com.lucky.api.controller.admin.vo.WechatUserListVO;
import com.lucky.api.controller.common.BaseController;
import com.lucky.api.utils.ResponseFormat;
import com.lucky.application.AdminUserServer;
import com.lucky.application.WechatUserServer;
import com.lucky.application.interceptor.LoginUserEntity;
import com.lucky.domain.entity.AdminUserEntity;
import com.lucky.domain.entity.WechatUserEntity;
import com.lucky.domain.exception.BusinessException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 后台用户
 *
 * @folder API/后台/用户
 */
@RestController
@RequestMapping
public class AdminUserController extends BaseController {
	private final AdminUserServer adminUserServer;
	private final WechatUserServer wechatUserServer;

	public AdminUserController(AdminUserServer adminUserServer, WechatUserServer wechatUserServer) {
		this.adminUserServer = adminUserServer;
		this.wechatUserServer = wechatUserServer;
	}

	/**
	 * 添加
	 *
	 * @param dto
	 * @return
	 */
	@PostMapping("/api/admin/user/save")
	@ResponseFormat
	public void save(@RequestBody AdminUserDTO dto) {
		var entity = AdminUserDTO.toEntity(dto);
		var id = adminUserServer.saveOrUpdate(entity);
		if (Objects.isNull(id))
			throw BusinessException.newInstance("添加失败");
	}

	/**
	 * 修改
	 *
	 * @param dto
	 * @return
	 */
	@PutMapping("/api/admin/user/update")
	@ResponseFormat
	public void update(@RequestBody AdminUserDTO dto) {
		var entity = AdminUserDTO.toEntity(dto);
		var id = adminUserServer.saveOrUpdate(entity);
		if (Objects.isNull(id))
			throw BusinessException.newInstance("修改失败");
	}

	/**
	 * 列表
	 *
	 * @param adminUserFindListDTO
	 * @return
	 */
	@PostMapping("/api/admin/user/list")
	@ResponseFormat
	public List<AdminUserVO> list(@RequestBody AdminUserFindListDTO adminUserFindListDTO) {
		var adminUserEntity = AdminUserEntity.builder()
				.name(adminUserFindListDTO.getName())
				.phone(adminUserFindListDTO.getPhone())
				.build();
		return adminUserServer.list(adminUserEntity).stream()
				.map(AdminUserVO::getInstance)
				.collect(Collectors.toList());
	}

	/**
	 * 启用禁用
	 */
	@PutMapping("/api/admin/user/enabled")
	@ResponseFormat
	public void enabled(@RequestBody EnabledDTO enabledDTO) {

		adminUserServer.enabled(enabledDTO.getId(), enabledDTO.getEnabled());

	}

	/**
	 * 登录
	 */
	@PostMapping("/api/admin/user/login")
	@ResponseFormat
	public LoginUserEntity login(@RequestBody LoginDTO loginDTO) {
		return adminUserServer.login(loginDTO.getPhone(), loginDTO.getPassword());
	}


	//微信用户管理


	/**
	 * 微信用户-列表
	 */
	@PutMapping("api/admin/wechat-user")
	@ResponseFormat
	public List<WechatUserListVO> list(@RequestBody  WechatUserFindListDTO dto) {

		var wechatUserEntity = WechatUserEntity.builder()
				.phone(dto.getPhone())
				.name(dto.getName())
				.build();

		return wechatUserServer.list(wechatUserEntity, dto.getOwnerName())
				.stream()
				.map(WechatUserListVO::getInstance)
				.collect(Collectors.toList());
	}

	/**
	 *  微信用户-增加用户余额
	 */
	@PutMapping("api/admin/wechat-user/balance")
	@ResponseFormat
	public void balanceAdd(@RequestBody BalanceDTO dto) {
		wechatUserServer.balanceAdd(dto.getWechatUserId(), dto.getMoney(), this.getAdminUserId());
	}


}
