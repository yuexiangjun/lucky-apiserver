package com.lucky.domain.valueobject;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SessionInfo {
	/**
	 * 场次id
	 */
	private Long id;
	/**
	 * 场次编号
	 */
	private Integer sessionNumber;
	/**
	 * 本场总库存
	 */
	private Integer totalInventory;
	/**
	 * 本场剩余库存
	 */
	private Integer remainInventory;
	/**
	 * 场次状态 0：禁用 1：启用 2：结束
	 */
	private Integer status;

	/**
	 * 商品详情
	 */
	private List<InventoryInfo> inventoryInfos;
	/**
	 * 抽奖结束时间 秒
	 */
	private Long endTime;
	/**
	 * 是否排队成功
	 */
	private Boolean isLineUpSuccess;


}
