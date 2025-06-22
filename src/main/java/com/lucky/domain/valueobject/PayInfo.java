package com.lucky.domain.valueobject;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PayInfo {
	/**
	 * 微信支付参数
	 */
	 private Map<String, Object> payParams;
	/**
	 * 支付订单id
	 */
	 private Long payOrderId;
}
