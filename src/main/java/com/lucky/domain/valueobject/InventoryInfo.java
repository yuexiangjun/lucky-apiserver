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
	 * 单价
	 */
	private BigDecimal price;
	/**
	 * 获取概率
	 */
	private  String  probability;

	public static String removeTrailingZeros(String numberStr) {
		if (numberStr == null || numberStr.isEmpty()) {
			return "0";
		}

		// 处理负号
		boolean isNegative = numberStr.startsWith("-");
		String num = isNegative ? numberStr.substring(1) : numberStr;

		// 分割整数和小数部分
		String integerPart;
		String fractionalPart = "";

		int dotIndex = num.indexOf('.');
		if (dotIndex != -1) {
			integerPart = num.substring(0, dotIndex);
			fractionalPart = num.substring(dotIndex + 1);
		} else {
			integerPart = num;
		}

		// 处理整数部分（去除前导零）
		integerPart = integerPart.replaceFirst("^0+(?!$)", "");
		if (integerPart.isEmpty()) {
			integerPart = "0";
		}

		// 处理小数部分（去除末尾零）
		fractionalPart = fractionalPart.replaceAll("0+$", "");

		// 组合结果
		String result;
		if (fractionalPart.isEmpty()) {
			result = integerPart;
		} else {
			result = integerPart + "." + fractionalPart;
		}

		// 处理结果为0的情况（忽略负号）
		if (result.equals("0")) {
			return "0";
		}

		// 添加负号
		return isNegative ? "-" + result : result;
	}



}
