package com.lucky.domain;

import cn.hutool.core.collection.CollectionUtil;
import com.lucky.domain.entity.LogisticsOrderEntity;
import com.lucky.domain.entity.LogisticsOrderPrizeEntity;
import com.lucky.domain.entity.OrderPrizeEntity;
import com.lucky.domain.enumes.NoCounterType;
import com.lucky.domain.repository.LogisticsOrderPrizeRepository;
import com.lucky.domain.repository.LogisticsOrderRepository;
import com.lucky.domain.valueobject.BaseDataPage;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class LogisticsOrderService {
	private final LogisticsOrderRepository logisticsOrderRepository;

	private final LogisticsOrderPrizeRepository logisticsOrderPrizeRepository;
	private final DeliveryAddressService deliveryAddressService;

	private final NoCounterService noCounterService;

	public LogisticsOrderService(LogisticsOrderRepository logisticsOrderRepository,
	                             LogisticsOrderPrizeRepository logisticsOrderPrizeRepository,
	                             DeliveryAddressService deliveryAddressService, NoCounterService noCounterService) {
		this.logisticsOrderRepository = logisticsOrderRepository;
		this.logisticsOrderPrizeRepository = logisticsOrderPrizeRepository;
		this.deliveryAddressService = deliveryAddressService;

		this.noCounterService = noCounterService;
	}

	/**
	 * 生成物流订单
	 *
	 * @param addressId
	 * @param orderPrizeEntities
	 * @param wechatUserId
	 */
	public void generateLogisticsOrder(Long addressId, List<OrderPrizeEntity> orderPrizeEntities, Long wechatUserId) {

		var deliveryAddressEntity = deliveryAddressService.getById(addressId);

		var logisticsOrderEntity = LogisticsOrderEntity
				.builder()
				.status(0)
				.wechatUserId(wechatUserId)
				.address(deliveryAddressEntity)
				.number(noCounterService.nextNo(NoCounterType.Logistics_order))
				.build();


		var id = logisticsOrderRepository.saveOrUpdate(logisticsOrderEntity);


		var logisticsOrderPrizes = orderPrizeEntities.stream()
				.map(s ->
						LogisticsOrderPrizeEntity.builder()
								.logisticsOrderId(id)
								.orderPrizeId(s.getId())
								.productId(s.getProductId())
								.build()
				).collect(Collectors.toList());

		logisticsOrderPrizeRepository.savaOrUpdateBatch(logisticsOrderPrizes);


	}

	/**
	 * 查询物流订单
	 */
	public List<LogisticsOrderEntity> getByWechatUserId(Long wechatUserId) {

		var logisticsOrderEntities = logisticsOrderRepository.getByWechatUserId(wechatUserId);

		if (CollectionUtil.isEmpty(logisticsOrderEntities))
			return List.of();

		var logisticsOrderIds = logisticsOrderEntities.stream()
				.map(LogisticsOrderEntity::getId)
				.collect(Collectors.toList());

		var logisticsOrderPrizeEntities = logisticsOrderPrizeRepository.getByLogisticsOrderIds(logisticsOrderIds);

		var LogisticsOrderPrizeMap = logisticsOrderPrizeEntities.stream()
				.collect(Collectors.groupingBy(LogisticsOrderPrizeEntity::getLogisticsOrderId));


		return logisticsOrderEntities.stream()
				.peek(s -> s.setGoods(LogisticsOrderPrizeMap.getOrDefault(s.getId(), List.of())))
				.collect(Collectors.toList());


	}

	/**
	 * 修改订单
	 */
	public void updateLogisticsOrder(LogisticsOrderEntity logisticsOrderEntity) {
		logisticsOrderRepository.saveOrUpdate(logisticsOrderEntity);
	}


	public List<LogisticsOrderEntity> getByAdminList(LogisticsOrderEntity entity) {
		var logisticsOrderEntities = logisticsOrderRepository.getByAdminList(entity);
		if (CollectionUtil.isEmpty(logisticsOrderEntities))
			return List.of();

		var logisticsOrderIds = logisticsOrderEntities.stream()
				.map(LogisticsOrderEntity::getId)
				.collect(Collectors.toList());

		var logisticsOrderPrizeEntities = logisticsOrderPrizeRepository.getByLogisticsOrderIds(logisticsOrderIds);

		var LogisticsOrderPrizeMap = logisticsOrderPrizeEntities.stream()
				.collect(Collectors.groupingBy(LogisticsOrderPrizeEntity::getLogisticsOrderId));


		return logisticsOrderEntities.stream()
				.peek(s -> s.setGoods(LogisticsOrderPrizeMap.getOrDefault(s.getId(), List.of())))
				.collect(Collectors.toList());

	}

	public BaseDataPage<LogisticsOrderEntity> getByAdminListPage(LogisticsOrderEntity entity, Integer page, Integer size) {
		var  dataPage= logisticsOrderRepository.getByAdminListPage(entity, page, size);

		List<LogisticsOrderEntity> logisticsOrderEntities = dataPage.getDataList();

		if (CollectionUtil.isEmpty(logisticsOrderEntities))
			return new BaseDataPage<>();

		var logisticsOrderIds = logisticsOrderEntities.stream()
				.map(LogisticsOrderEntity::getId)
				.collect(Collectors.toList());

		var logisticsOrderPrizeEntities = logisticsOrderPrizeRepository.getByLogisticsOrderIds(logisticsOrderIds);

		var LogisticsOrderPrizeMap = logisticsOrderPrizeEntities.stream()
				.collect(Collectors.groupingBy(LogisticsOrderPrizeEntity::getLogisticsOrderId));


		List<LogisticsOrderEntity> collect = logisticsOrderEntities.stream()
				.peek(s -> s.setGoods(LogisticsOrderPrizeMap.getOrDefault(s.getId(), List.of())))
				.collect(Collectors.toList());

		return BaseDataPage.newInstance(dataPage.getTotal(), dataPage.getPages(), collect);
	}
}
