package com.lucky.domain;

import cn.hutool.core.collection.CollectionUtil;
import com.lucky.domain.entity.OrderEntity;
import com.lucky.domain.entity.OrderPrizeEntity;
import com.lucky.domain.exception.BusinessException;
import com.lucky.domain.repository.OrderPrizeRepository;
import com.lucky.domain.repository.OrderRepository;
import com.lucky.domain.valueobject.BaseDataPage;
import com.lucky.domain.valueobject.PrizeInfoNum;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class OrderService {
	private final OrderRepository orderRepository;
	private final OrderPrizeRepository orderPrizeRepository;

	public OrderService(OrderRepository orderRepository, OrderPrizeRepository orderPrizeRepository) {
		this.orderRepository = orderRepository;
		this.orderPrizeRepository = orderPrizeRepository;
	}

	/**
	 * 添加/修改订单
	 */
	public Long saveOrUpdate(OrderEntity entity) {
		return orderRepository.saveOrUpdate(entity);
	}

	/**
	 * 列表
	 */
	public List<OrderEntity> list(OrderEntity entity) {
		var orderEntities = orderRepository.list(entity);
		return this.getOrderEntities(orderEntities);
	}

	/**
	 * 更据id查询
	 */
	public OrderEntity getById(Long id) {
		if (Objects.isNull(id))
			return null;
		var order = orderRepository.getById(id);

		var orderEntities = new ArrayList<OrderEntity>();
		orderEntities.add(order);

		return CollectionUtils.firstElement(this.getOrderEntities(orderEntities));

	}

	/**
	 * 修改状态
	 */
	public void updateStatus(Long id, Integer status) {
		if (Objects.isNull(id))
			throw BusinessException.newInstance("id不能为空");
		var order = OrderEntity.builder()
				.id(id)
				.status(status)
				.build();
		if (Objects.equals(1, status))
			order.setSendTime(LocalDateTime.now());
		if (Objects.equals(2, status))
			order.setFinishTime(LocalDateTime.now());

		orderRepository.saveOrUpdate(order);
	}

	public BaseDataPage<OrderEntity> page(OrderEntity entity, Integer page, Integer size) {

		var orderPage = orderRepository.page(entity, page, size);

		var orderEntities = orderPage.getDataList();

		if (CollectionUtils.isEmpty(orderEntities))
			return orderPage;

		orderEntities = this.getOrderEntities(orderEntities);

		orderPage.setDataList(orderEntities);

		return orderPage;
	}


	public List<OrderEntity> findByTopicId(Long topicId) {
		var orderEntities = orderRepository.list(OrderEntity.builder().topicId(topicId).build());

		return this.getOrderEntities(orderEntities);


	}

	private List<OrderEntity> getOrderEntities(List<OrderEntity> orderEntities) {
		if (CollectionUtils.isEmpty(orderEntities))
			return List.of();

		var orderIds = orderEntities.stream()
				.map(OrderEntity::getId)
				.collect(Collectors.toList());

		var orderPrizeEntities = orderPrizeRepository.findByOrderIdIn(orderIds);

		if (CollectionUtils.isEmpty(orderPrizeEntities))
			return orderEntities;

		var orderPrizeMap = orderPrizeEntities
				.stream()
				.collect(Collectors.groupingBy(OrderPrizeEntity::getOrderId));

		return orderEntities.stream()
				.peek(s -> s.setOrderPrizeEntities(orderPrizeMap.getOrDefault(s.getId(), List.of())))
				.collect(Collectors.toList());
	}

	/**
	 * 添加订单
	 */
	public void save(List<Long> prizeIds,
	                 Long topicId,
	                 Long sessionId,
	                 Long wechatUserId,
	                 Long payOrderId) {

		var orderEntity = OrderEntity.builder()
				.payOrderId(payOrderId)
				.topicId(topicId)
				.sessionId(sessionId)
				.wechatUserId(wechatUserId)
				.status(0)
				.createTime(LocalDateTime.now())
				.build();

		var orderId = orderRepository.saveOrUpdate(orderEntity);

		var orderPrizeEntities = prizeIds.stream()
				.map(s -> OrderPrizeEntity.builder()
						.orderId(orderId)
						.wechatUserId(wechatUserId)
						.productId(s)
						.sessionId(sessionId)
						.createTime(LocalDateTime.now())
						.build()
				).collect(Collectors.toList());

		orderPrizeRepository.saveOrUpdateBatch(orderPrizeEntities);

	}

	public List<OrderPrizeEntity> findByPrizeIds(List<Long> prizeInfoIds) {

		return orderPrizeRepository.findByPrizeIds(prizeInfoIds, null);
	}

	public List<OrderPrizeEntity> findByWechatUserId(Long wechatUserId, Boolean isDelivery) {
		return orderPrizeRepository.findByWechatUserId(wechatUserId, isDelivery);
	}

	public List<OrderPrizeEntity> deductionInventory(List<PrizeInfoNum> goods, Long wechatUserId) {

		var prizeInfoNumMap = goods.stream()
				.collect(Collectors.toMap(PrizeInfoNum::getId, PrizeInfoNum::getNum));


		var prizeInfoIds = prizeInfoNumMap.keySet().stream()
				.collect(Collectors.toList());

		var orderPrizeEntities = orderPrizeRepository.findByPrizeIdsAndWechatUserId(prizeInfoIds, false, wechatUserId);

		var deductionOrderPrize = new ArrayList<List<OrderPrizeEntity>>();

		var orderPrizeMap = orderPrizeEntities.stream()
				.collect(Collectors.groupingBy(OrderPrizeEntity::getProductId));

		for (Map.Entry<Long, List<OrderPrizeEntity>> longListEntry : orderPrizeMap.entrySet()) {
			//订单商品
			List<OrderPrizeEntity> value = longListEntry.getValue();

			//扣除数量

			Integer num = prizeInfoNumMap.get(longListEntry.getKey());

			if (num >= value.size()) {
				deductionOrderPrize.add(value);
			} else {
				List<OrderPrizeEntity> collect = value.stream()
						.sorted(Comparator.comparing(OrderPrizeEntity::getCreateTime))
						.limit(num)
						.collect(Collectors.toList());

				deductionOrderPrize.add(collect);
			}


		}


		var orderPrizeEntityList = deductionOrderPrize.stream()
				.filter(CollectionUtil::isNotEmpty)
				.flatMap(List::stream)
				.peek(s -> s.setIsDelivery(true))
				.collect(Collectors.toList());
		orderPrizeRepository.saveOrUpdateBatch(orderPrizeEntityList);

		return orderPrizeEntityList;


	}


	public List<OrderEntity> listIds(OrderEntity entity, List<Long> wechatUserIds, List<Long> seriesIds) {
		var orderEntities = orderRepository.listIds(entity, wechatUserIds, seriesIds);
		return this.getOrderEntities(orderEntities);
	}

	/**
	 * 获取该商品才本系列是否中将
	 */
	public Boolean boxCabinets(Long topicId, Long productId) {

		var orderPrizeEntities = orderPrizeRepository.findByPrizeIds(List.of(productId), null);
		if (CollectionUtils.isEmpty(orderPrizeEntities))
			return false;
		var orderIds = orderPrizeEntities
				.stream()
				.map(OrderPrizeEntity::getOrderId)
				.distinct()
				.collect(Collectors.toList());

		var orderEntities = orderRepository.findByIds(orderIds)
				.stream()
				.filter(s -> Objects.equals(s.getTopicId(), topicId))
				.collect(Collectors.toList());

		if (CollectionUtils.isEmpty(orderEntities))
			return false;
		return true;


	}

	public BaseDataPage<OrderEntity> listPage(OrderEntity entity, List<Long> findWechatUserIds, List<Long> seriesIds, Integer page, Integer size) {
		BaseDataPage<OrderEntity> listPage = orderRepository.listPage(entity, findWechatUserIds, seriesIds, page, size);

		var orderEntities = listPage.getDataList();

		 var orderEntityList =this.getOrderEntities(orderEntities);
		 listPage.setDataList(orderEntityList);
		 return listPage;


	}
}
