package com.lucky.infrastructure.repository;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lucky.domain.entity.OrderPrizeEntity;
import com.lucky.domain.repository.OrderPrizeRepository;
import com.lucky.infrastructure.repository.mysql.mapper.OrderPrizeMapper;
import com.lucky.infrastructure.repository.mysql.po.OrderPrizePO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 订单关联商品
 */
@Component
public class OrderPrizeRepositoryImpl extends ServiceImpl<OrderPrizeMapper, OrderPrizePO> implements OrderPrizeRepository {

	@Override
	public void saveOrUpdateBatch(List<OrderPrizeEntity> orderPrizeEntities) {
		var orderPrizePOS = orderPrizeEntities.stream()
				.map(s -> OrderPrizePO.getInstance(s))
				.collect(Collectors.toList());

		this.saveOrUpdateBatch(orderPrizePOS);
	}

	@Override
	public List<OrderPrizeEntity> findByOrderIdIn(List<Long> orderIds) {
		var orderPrizePOLambdaQueryWrapper = Wrappers.lambdaQuery(OrderPrizePO.class)
				.in(OrderPrizePO::getOrderId, orderIds);
		return list(orderPrizePOLambdaQueryWrapper)
				.stream()
				.map(OrderPrizePO::toEntity)
				.collect(Collectors.toList());
	}

	@Override
	public List<OrderPrizeEntity> findByPrizeIds(List<Long> prizeInfoIds, Boolean isDelivery) {
		if (CollectionUtils.isEmpty(prizeInfoIds))
			return List.of();
		var orderPrizePOLambdaQueryWrapper = Wrappers.lambdaQuery(OrderPrizePO.class)
				.in(OrderPrizePO::getProductId, prizeInfoIds)
				.eq(OrderPrizePO::getIsDelivery, isDelivery);
		return list(orderPrizePOLambdaQueryWrapper)
				.stream()
				.map(OrderPrizePO::toEntity)
				.collect(Collectors.toList());
	}

	@Override
	public List<OrderPrizeEntity> findByWechatUserId(Long wechatUserId, Boolean isDelivery) {

		var orderPrizePOLambdaQueryWrapper = Wrappers.lambdaQuery(OrderPrizePO.class)
				.eq(OrderPrizePO::getWechatUserId, wechatUserId)
				.eq(Objects.nonNull(isDelivery), OrderPrizePO::getIsDelivery, isDelivery);

		return list(orderPrizePOLambdaQueryWrapper)
				.stream()
				.map(OrderPrizePO::toEntity)
				.collect(Collectors.toList());
	}

	@Override
	public List<OrderPrizeEntity> findByPrizeIdsAndWechatUserId(List<Long> prizeInfoIds, Boolean isDelivery, Long wechatUserId) {
		if (CollectionUtils.isEmpty(prizeInfoIds))
			return List.of();
		var orderPrizePOLambdaQueryWrapper = Wrappers.lambdaQuery(OrderPrizePO.class)
				.in(OrderPrizePO::getProductId, prizeInfoIds)
				.eq(OrderPrizePO::getWechatUserId, wechatUserId)
				.eq(OrderPrizePO::getIsDelivery, isDelivery);
		return list(orderPrizePOLambdaQueryWrapper)
				.stream()
				.map(OrderPrizePO::toEntity)
				.collect(Collectors.toList());
	}
}
