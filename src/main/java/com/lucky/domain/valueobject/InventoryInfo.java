package com.lucky.domain.valueobject;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InventoryInfo {
	/**
	 * 奖品id
	 */
	private Long prizeId;
	/**
	 * 总库存
	 */
	private Integer totalInventory;
	/**
	 * 剩余库存
	 */
	private Integer remainInventory;
	/**
	 * 奖品名称
	 */
	private String prizeName;
	/**
	 * 奖品图片
	 */
	private String prizeUrl;
	/**
	 * 奖品等级名称
	 */
	private String gradeName;
	/**
	 * 排序
	 */
	private Integer sort;
	/**
	 * 获取概率
	 */
	private  String  probability;

}
