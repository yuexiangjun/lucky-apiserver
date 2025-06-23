package com.lucky.api.controller.external;

import com.lucky.api.controller.common.BaseController;
import com.lucky.api.utils.ResponseFormat;
import com.lucky.application.SessionInfoServer;

import com.lucky.domain.valueobject.BaseDataPage;
import com.lucky.domain.valueobject.SessionInfo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 订单记录
 *
 * @folder API/小程序/首页详情
 */
@RestController
@RequestMapping("/api/wechat/detail")
public class SessionInfoController  extends BaseController {
	private final SessionInfoServer SessionInfoServer;

	public SessionInfoController(SessionInfoServer sessionInfoServer) {
		SessionInfoServer = sessionInfoServer;
	}

	/**
	 * 场次商品详情
	 */
	@GetMapping("/product-details")
	@ResponseFormat
	public BaseDataPage<SessionInfo> findByTopicIdPageNO(@RequestParam Long topicId, @RequestParam Integer page, @RequestParam Integer size) {

		return SessionInfoServer.findByTopicIdPageNO(topicId, page, size,this.getWechatUserId());


	}

	/**
	 * 全部场次
	 */
	@GetMapping("/session-list")
	@ResponseFormat
	public BaseDataPage<SessionInfo> findByTopicIdPageStatus(@RequestParam Long topicId, @RequestParam Integer page, @RequestParam Integer size) {
		return SessionInfoServer.findByTopicIdPageStatus(topicId, page, size,this.getWechatUserId());
	}


}
