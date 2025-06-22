package com.lucky.api.controller.external.dto;

import cn.hutool.core.bean.BeanUtil;
import com.lucky.domain.entity.DeliveryAddressEntity;
import com.lucky.domain.exception.BusinessException;
import lombok.Data;

import java.util.Objects;

/**
 * 收货地址信息
 */
@Data
public class DeliveryAddressDTO {
	/**
	 * id
	 */
	private Long id;
	/**
	 * 收货人名
	 */
	private String name;
	/**
	 * 收货人手机号
	 */
	private String phone;
	/**
	 * 省
	 */
	private String province;
	/**
	 * 市
	 */
	private String city;
	/**
	 * 区
	 */
	private String area;
	/**
	 * 收货人地址
	 */
	private String address;
	/**
	 * 是否默认
	 */
	private Boolean isDefault;

	public static DeliveryAddressEntity toEntity(DeliveryAddressDTO dto, Long wechatUserId) {

		if (Objects.isNull(dto))
			throw BusinessException.newInstance("参数为空");

		DeliveryAddressEntity bean = BeanUtil.toBean(dto, DeliveryAddressEntity.class);
		bean.setWechatUserId(wechatUserId);
		return bean;

	}
}
