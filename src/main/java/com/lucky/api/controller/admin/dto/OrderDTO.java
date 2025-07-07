package com.lucky.api.controller.admin.dto;

import cn.hutool.core.bean.BeanUtil;
import com.lucky.domain.entity.OrderEntity;
import com.lucky.domain.exception.BusinessException;
import lombok.*;

import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDTO {
	/**
	 * 用户名字或者电话号码
	 */
	private String userNameOrPhone;
	/**
	 * 系列名字
	 */
	private String seriesName;

	/**
	 * 微信用户id
	 */
	private Long wechatUserId;
	/**
	 * 主题id
	 */
	private Long topicId;
	/**
	 * 场次id
	 */
	private Long sessionId;

	/**
	 * 订单状态  0-待发货 1-待收货 2-完成
	 */
	private Integer status;

	/**
	 * 页
	 */
	private Integer page;
	/**
	 * 条数
	 */
	private Integer size;

	/**
	 * @param dto
	 * @return
	 */

	public static OrderEntity toEntity(OrderDTO dto) {
		if (Objects.isNull(dto))
			throw BusinessException.newInstance("参数缺失");
		return BeanUtil.toBean(dto, OrderEntity.class);

	}


}
