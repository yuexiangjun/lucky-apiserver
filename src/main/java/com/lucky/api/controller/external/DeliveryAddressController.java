package com.lucky.api.controller.external;

import com.lucky.api.controller.common.BaseController;
import com.lucky.api.controller.external.dto.DeliveryAddressDTO;
import com.lucky.api.controller.external.vo.DeliveryAddressVO;
import com.lucky.api.utils.ResponseFormat;
import com.lucky.application.DeliveryAddressServer;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 收货地址信息
 *
 * @folder API/小程序/收货地址信息
 */
@RequestMapping("/wechat/delivery-address")
@RestController
public class DeliveryAddressController extends BaseController {
	private final DeliveryAddressServer deliveryAddressServer;

	public DeliveryAddressController(DeliveryAddressServer deliveryAddressServer) {
		this.deliveryAddressServer = deliveryAddressServer;
	}


	/**
	 * 添加修改
	 *
	 * @return
	 */
	@ResponseFormat
	@PostMapping()
	public void saveOrUpdate(@RequestBody DeliveryAddressDTO dto) {
		var entity = DeliveryAddressDTO.toEntity(dto, this.getWechatUserId());
		deliveryAddressServer.saveOrUpdate(entity);

	}


	/**
	 * 更据id删除
	 */
	@ResponseFormat
	@DeleteMapping()
	public void deleteById(@RequestParam Long id) {
		deliveryAddressServer.deleteById(id);
	}

	/**
	 * 更据用户查询
	 */
	@ResponseFormat
	@GetMapping("/list")
	public List<DeliveryAddressVO> getByWechatUserId(@RequestParam(required = false) Long wechatUserId) {
		return deliveryAddressServer.getByWechatUserId(Objects.isNull(wechatUserId) ? this.getWechatUserId() : wechatUserId)
				.stream()
				.map(DeliveryAddressVO::getInstance)
				.collect(Collectors.toList());
	}

	/**
	 * 修改默认地址
	 */
	@PutMapping("/default")
	@ResponseFormat
	public void updateDefault(@RequestParam Long id, @RequestParam(required = false) Long wechatUserId) {
		deliveryAddressServer.updateDefault(id, Objects.isNull(wechatUserId) ? this.getWechatUserId() : wechatUserId);

	}


}
