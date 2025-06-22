package com.lucky.api.controller.external;

import com.lucky.api.controller.admin.dto.OrderDTO;
import com.lucky.api.controller.common.BaseController;
import com.lucky.api.controller.external.dto.PrizePublicityDTO;
import com.lucky.api.controller.external.vo.PrizePublicityVO;
import com.lucky.api.controller.external.vo.WechatOrderListVO;
import com.lucky.api.utils.ResponseFormat;
import com.lucky.application.OrderServer;

import com.lucky.domain.valueobject.BaseDataPage;
import com.lucky.domain.valueobject.Order;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 订单记录
 *
 * @folder API/小程序/订单记录
 */
@RestController
@RequestMapping("/wechat/order")
public class WechatOrderController extends BaseController {
	private final OrderServer orderServer;

	public WechatOrderController(OrderServer orderServer) {
		this.orderServer = orderServer;
	}


	/**
	 * 抽奖记录
	 */
	@PostMapping("/list")
	@ResponseFormat
	public BaseDataPage<Order> list(@RequestBody OrderDTO dto) {

		if (Objects.isNull(dto.getWechatUserId()))
			dto.setWechatUserId(this.getWechatUserId());

		var entity = OrderDTO.toEntity(dto);

		if (Objects.isNull(entity.getWechatUserId()))
			entity.setWechatUserId(this.getWechatUserId());

		return orderServer.page(entity, dto.getPage(), dto.getSize());

	}

	/**
	 * 中奖公示
	 */
	@PostMapping("/prize-publicity")
	@ResponseFormat
	public List<PrizePublicityVO> prizePublicity(@RequestBody PrizePublicityDTO dto) {

		var publicityList = orderServer.prizePublicity(dto.getGradeType(), dto.getTopicId());
		return publicityList.stream()
				.map(PrizePublicityVO::toVO)
				.collect(Collectors.toList());
	}

	/**
	 * star 福禄
	 */
	@GetMapping("/get-prize-info")
	@ResponseFormat
	public List<WechatOrderListVO> getPrizeInfo(@RequestParam(required = false) Long wechatUserId) {

		if (Objects.isNull(wechatUserId))
			wechatUserId =this.getWechatUserId();
		return orderServer.getPrizeInfo(wechatUserId)
				.stream()
				.map(WechatOrderListVO::toVO)
				.collect(Collectors.toList());

	}


}
