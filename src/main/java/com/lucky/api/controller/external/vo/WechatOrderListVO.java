package com.lucky.api.controller.external.vo;

import cn.hutool.core.bean.BeanUtil;
import com.lucky.domain.valueobject.WechatOrderList;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class WechatOrderListVO {
	/**
	 * id
	 */
	private Long id;
	/**
	 * 系列名称
	 */
	private String seriesName;

	/**
	 * 时间
	 */
	private LocalDateTime createTime;
	/**
	 * 状态 2：完成
	 */
	private Integer status;
	/**
	 * 商品详情
	 */
	/**
	 * 商品信息
	 */
	private List<WechatPrizeInfoVO> goods;

	public static WechatOrderListVO toVO(WechatOrderList wechatOrderList) {
		if (Objects.isNull(wechatOrderList))
			return null;
		 var bean = BeanUtil.toBean(wechatOrderList, WechatOrderListVO.class);
		var goods= wechatOrderList.getGoods()
				 .stream()
				 .map(WechatPrizeInfoVO::getInstance)
				 .collect(Collectors.toList());
		 bean.setGoods(goods);
		 return bean;

	}
}
