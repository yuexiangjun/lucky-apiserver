package com.lucky.infrastructure.repository.mysql.po;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.lucky.domain.entity.PayOrderEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@TableName(value = "pay_order", autoResultMap = true)
public class PayOrderPO {
	/**
	 * id
	 */
	@TableId(value = "id", type = IdType.ASSIGN_ID)
	private Long id;

	/**
	 * 支付金额
	 */
	private BigDecimal payMoney;
	/**
	 * 支付状态
	 */
	private Integer payStatus;
	/**
	 * 支付类型 1 微信支付 2：平台积分支付
	 */
	private Integer payType;
	/**
	 * 订单类型 1 抽奖订单 2：充值订单
	 */
	private Integer orderType;

	/**
	 * 支付人
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
	 * 抽奖次数
	 */
	private Integer times;
	/**
	 * 支付时间
	 */
	private LocalDateTime payTime;
	/**
	 * 总金额
	 */
	private BigDecimal totalMoney;
	/**
	 * 完成时间
	 */
	private LocalDateTime completeTime;
	/**
	 * 三方支付id
	 */
	private String ThirdPayId;
	/**
	 * 商品id
	 */
	@TableField(typeHandler = JacksonTypeHandler.class)
	private List<Long> prizeId;
	/**
	 * 支付前端需要的参数
	 */
	private String payParams;

	public static PayOrderPO getInstance(PayOrderEntity entity) {
		if (Objects.isNull(entity))
			return null;

		return BeanUtil.toBean(entity, PayOrderPO.class);
	}

	public static PayOrderEntity toEntity(PayOrderPO po) {
		if (Objects.isNull(po))
			return null;

		return BeanUtil.toBean(po, PayOrderEntity.class);
	}
}
