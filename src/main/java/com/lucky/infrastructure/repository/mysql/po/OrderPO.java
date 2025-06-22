package com.lucky.infrastructure.repository.mysql.po;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.lucky.domain.entity.OrderEntity;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("`order`")
public class OrderPO  {
	/**
	 * 订单id
	 */
	@TableId(value = "id", type = IdType.ASSIGN_ID)
	private Long id;
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
	 * 支付订单id
	 */
	private Long payOrderId;
	/**
	 * 商品id
	 */
	private Long productId;
	/**
	 * 订单状态  0-待发货 1-待收货 2-完成
	 */
	private Integer status;
	/**
	 * 订单创建时间
	 */
	private LocalDateTime createTime;
	/**
	 * 完成时间
	 */
	private LocalDateTime finishTime;
	/**
	 * 发货时间
	 */
	private LocalDateTime sendTime;


	public static OrderPO getInstance(OrderEntity entity) {
		if (Objects.isNull(entity))
			return null;
		return BeanUtil.toBean(entity, OrderPO.class);
	}

	public static OrderEntity toEntity(OrderPO po) {
		if (Objects.isNull(po))
			return null;
		return BeanUtil.toBean(po, OrderEntity.class);
	}


}
