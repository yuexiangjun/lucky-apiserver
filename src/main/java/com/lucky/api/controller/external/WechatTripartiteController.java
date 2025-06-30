package com.lucky.api.controller.external;

import com.lucky.api.controller.external.vo.Code2SessionVO;
import com.lucky.api.utils.ResponseFormat;
import com.lucky.application.WechatUserServer;
import com.lucky.application.tripartite.WechatServer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 小程序的三方接口
 *
 * @folder API/小程序/小程序的三方接口
 */
@RestController
@RequestMapping("/api/wechat/tripartite")
public class WechatTripartiteController {
	private final WechatServer wechatServer;
	private final WechatUserServer wechatUserServer;

	public WechatTripartiteController(WechatServer wechatServer, WechatUserServer wechatUserServer) {
		this.wechatServer = wechatServer;
		this.wechatUserServer = wechatUserServer;
	}


	/**
	 * 小程序登录参数
	 *
	 * @param jsCode  小程序登录code
	 * @param ownerId 邀请人id
	 * @return
	 */
	@GetMapping("/login/js-code")
	@ResponseFormat
	public Code2SessionVO code2Session(@RequestParam String jsCode,
	                                   @RequestParam(value = "ownerId", required = false) Long ownerId) {
		var code2Session = wechatServer.code2Session(jsCode);
		return Code2SessionVO.builder()
				.authorization(code2Session.getAuthorization())
				.wechatUserId(code2Session.getWechatUserId())
				.build();
	}


	/**
	 * 根据jscode 和手机组件code注册
	 *
	 * @param jsCode    小程序登录code
	 * @param phoneCode 获取手机号码的code
	 * @param ownerId   邀请人id
	 * @return
	 */
	@GetMapping("/register")
	@ResponseFormat
	public Code2SessionVO register(@RequestParam String jsCode,
	                               @RequestParam String phoneCode,
	                               @RequestParam(value = "ownerId", required = false) Long ownerId) {
		var code2Session = wechatServer.register2(jsCode, phoneCode, ownerId);
		return Code2SessionVO.builder()
				.authorization(code2Session.getAuthorization())
				.wechatUserId(code2Session.getWechatUserId())
				.build();

	}


}
