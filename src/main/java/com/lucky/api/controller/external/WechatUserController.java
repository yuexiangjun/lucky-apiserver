package com.lucky.api.controller.external;

import com.lucky.api.controller.admin.vo.WechatUserListVO;
import com.lucky.api.controller.common.BaseController;
import com.lucky.api.controller.external.dto.WechatUserInfoDTO;
import com.lucky.api.controller.external.vo.WechatUserInfoVO;
import com.lucky.api.utils.ResponseFormat;
import com.lucky.application.WechatUserServer;
import com.lucky.application.interceptor.LoginUserEntity;
import com.lucky.domain.entity.WechatUserEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 小程序的业务系统用户
 *
 * @folder API/小程序/业务系统用户
 */
@RestController
@RequestMapping
public class WechatUserController extends BaseController {
	private final WechatUserServer wechatUserServer;

	public WechatUserController(WechatUserServer wechatUserServer) {
		this.wechatUserServer = wechatUserServer;
	}


	/**
	 * 登录注册
	 */
	@PostMapping("/api/wechat/user/login-enroll")
	@ResponseFormat
	public LoginUserEntity login(@RequestBody WechatUserInfoDTO dto) {

		var entity = WechatUserInfoDTO.toEntity(dto);

		return wechatUserServer.login(entity);

	}

	/**
	 * 获取用户信息
	 */
	@GetMapping("/api/wechat/user/get-info")
	@ResponseFormat
	public WechatUserInfoVO getInfo() {
		var entity = wechatUserServer.getInfo(this.getWechatUserId());
		return WechatUserInfoVO.toEntity(entity);

	}

	/**
	 * 修改信息
	 */
	@PutMapping("/api/wechat/user/update-info")
	@ResponseFormat
	public void updateInfo(@RequestBody WechatUserInfoDTO dto) {
		var entity = WechatUserInfoDTO.toEntity(dto);
		entity.setId(this.getWechatUserId());
		wechatUserServer.saveOrUpdate(entity);
	}

	/**
	 * 获取当前登录人的邀请人列表
	 */
	@GetMapping("/api/wechat/user/get-invite-list")
	@ResponseFormat
	public List<WechatUserListVO> getInviteList(@RequestParam(required = false) Long wechatUserId) {
		if (wechatUserId == null)
			wechatUserId = this.getWechatUserId();

		var entity = WechatUserEntity.builder()
				.ownerId(wechatUserId)
				.build();

		return wechatUserServer.list(entity, null)
				.stream()
				.map(WechatUserListVO::getInstance)
				.collect(Collectors.toList());

	}

}
