package com.lucky.application;

import com.lucky.domain.*;
import com.lucky.domain.config.RedissionConfig;
import com.lucky.domain.entity.*;

import com.lucky.domain.exception.BusinessException;
import com.lucky.domain.valueobject.*;
import lombok.Getter;
import org.apache.logging.log4j.util.Strings;
import org.redisson.api.RLock;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@Getter
public class OrderServer {
	private final OrderService orderService;
	private final PrizeInfoService prizeInfoService;
	private final GradeService gradeService;
	private final SeriesTopicService seriesTopicService;
	private final WechatUserService wechatUserService;
	private final SessionInfoService sessionInfoService;

	private final LogisticsOrderService logisticsOrderService;

	private final RedissionConfig redissionConfig;
	private final static String DEDUCTION = "DEDUCTION:";

	public OrderServer(OrderService orderService,
	                   PrizeInfoService prizeInfoService,
	                   GradeService gradeService,
	                   SeriesTopicService seriesTopicService,
	                   WechatUserService wechatUserService, SessionInfoService sessionInfoService, LogisticsOrderService logisticsOrderService, RedissionConfig redissionConfig) {
		this.orderService = orderService;
		this.prizeInfoService = prizeInfoService;
		this.gradeService = gradeService;
		this.seriesTopicService = seriesTopicService;
		this.wechatUserService = wechatUserService;
		this.sessionInfoService = sessionInfoService;

		this.logisticsOrderService = logisticsOrderService;
		this.redissionConfig = redissionConfig;
	}


	/**
	 * 列表
	 */
	public List<OrderEntity> list(OrderEntity entity) {
		return orderService.list(entity);
	}


	/**
	 * 修改订单状态
	 */
	public void updateStatus(Long id, Integer status) {

		orderService.updateStatus(id, status);
	}

	public BaseDataPage<Order> page(OrderEntity entity, Integer page, Integer size) {

		BaseDataPage<OrderEntity> orderEntityPage = orderService.page(entity, page, size);

		var dataList = orderEntityPage.getDataList();

		if (CollectionUtils.isEmpty(dataList))
			return new BaseDataPage<>(0l);
		//主题id
		var topicIds = dataList.stream()
				.map(OrderEntity::getTopicId)
				.collect(Collectors.toList());

		//商品id
		var productIds = dataList.stream()
				.map(s -> s.getOrderPrizeEntities()
						.stream()
						.map(OrderPrizeEntity::getProductId)
						.collect(Collectors.toList())
				)
				.flatMap(List::stream)
				.collect(Collectors.toList());

		var wechatUserIds = dataList.stream()
				.map(OrderEntity::getWechatUserId)
				.collect(Collectors.toList());

		var seriesTopicByName = seriesTopicService.findByIds(topicIds)
				.stream()
				.collect(Collectors.toMap(SeriesTopicEntity::getId, SeriesTopicEntity::getName));

		var products = prizeInfoService.findByIds(productIds);

		var gradeIds = products.stream()
				.map(PrizeInfoEntity::getGradeId)
				.collect(Collectors.toList());

		var prizeInfoMap = products.stream()
				.collect(Collectors.toMap(PrizeInfoEntity::getId, Function.identity()));

		var gradeEntityByName = gradeService.findByIds(gradeIds)
				.stream()
				.collect(Collectors.toMap(GradeEntity::getId, GradeEntity::getName));

		List<WechatUserEntity> wechatUsers = wechatUserService.getByIds(wechatUserIds);
		var wechatUserMap = wechatUsers.stream()
				.collect(Collectors.toMap(WechatUserEntity::getId, s -> {
					if (Strings.isBlank(s.getName())) {
						return "用户-".concat(s.getPhone().substring(0, 3)).concat("***");

					} else return s.getName();
				}));


		return BaseDataPage.newInstance(
				orderEntityPage.getTotal(),
				orderEntityPage.getPages(),
				dataList.stream()
						.map(orderEntity ->
								orderEntity.getOrderPrizeEntities()
										.stream()
										.map(orderPrizeEntity -> {

											var prizeInfoEntity = prizeInfoMap.get(orderPrizeEntity.getProductId());
											return Order
													.builder()
													.id(orderEntity.getId())
													.finishTime(orderEntity.getFinishTime())
													.createTime(orderEntity.getCreateTime())
													.sendTime(orderEntity.getSendTime())
													.status(orderEntity.getStatus())
													.wechatName(wechatUserMap.get(orderEntity.getWechatUserId()))
													.topicName(seriesTopicByName.get(orderEntity.getTopicId()))
													.productName(prizeInfoEntity.getPrizeName())
													.productUrl(prizeInfoEntity.getPrizeUrl())
													.sessionName(gradeEntityByName.get(prizeInfoEntity.getGradeId()))
													.build();
										}).collect(Collectors.toList())

						)
						.flatMap(List::stream)
						.collect(Collectors.toList()));

	}

	public PayOrderEntity getByPrizeInfo(PayOrderEntity payOrderEntity) {
		RLock lock = redissionConfig.redissonClient().getLock(DEDUCTION + payOrderEntity.getId());
		lock.lock();
		try {
			//获取主题下的商品
			var prizeInfoEntities = prizeInfoService.findByTopicId(payOrderEntity.getTopicId());

			var gradeIds = prizeInfoEntities.stream()
					.map(PrizeInfoEntity::getGradeId)
					.collect(Collectors.toList());

			List<GradeEntity> gradeEntities = gradeService.findByIds(gradeIds);

			var gradeEntityMap = gradeEntities.stream()
					.collect(Collectors.toMap(GradeEntity::getId, Function.identity()));


			var sessionInfoEntity = sessionInfoService.findById(payOrderEntity.getSessionId());


			var prizeInfoMap = prizeInfoEntities.stream()
					.collect(Collectors.groupingBy(PrizeInfoEntity::getType));


			//获取隐藏级的概率
			var hide = prizeInfoMap.get(1);

			//获取普通级的概率
			var common = prizeInfoMap.get(2);
			//获取总库存
			var prizeInventory = sessionInfoEntity.getPrizeInventory();
			//当前库存
			var currentInventory = prizeInventory
					.stream()
					.map(Inventory::getInventory)
					.reduce(0, Integer::sum);

			if (currentInventory < payOrderEntity.getTimes())
				throw BusinessException.newInstance("库存小于次数");

			//抽中的奖品id

			var prizeIds = new ArrayList<Long>();


			for (Integer i = 0; i < payOrderEntity.getTimes(); i++) {
				//当前库存
				currentInventory = prizeInventory
						.stream()
						.map(Inventory::getInventory)
						.reduce(0, Integer::sum);


				var prizeId = this. getaPrizeId(hide, gradeEntityMap, prizeInventory, currentInventory);

				prizeInventory = prizeInventory
						.stream()
						.map(entry -> {
							if (Objects.equals(entry.getPrizeId(), prizeId))
								entry.setInventory(entry.getInventory() - 1);
							return entry;
						})
						.collect(Collectors.toList());

				prizeIds.add(prizeId);
			}
			//检查是否存在隐藏存在就再补一次普通奖品

			prizeInventory	=this.checkSupplements(prizeIds,hide,prizeInventory);



			//库存等于0则修改状态为已抽完
			var reduce = prizeInventory.stream()
					.map(Inventory::getInventory)
					.reduce(0, Integer::sum);

			if (Objects.equals(reduce, 0))
				sessionInfoEntity.setStatus(2);

			sessionInfoEntity.setPrizeInventory(prizeInventory);

			sessionInfoService.saveOrUpdate(sessionInfoEntity);

			orderService.save(prizeIds,
					payOrderEntity.getTopicId(),
					payOrderEntity.getSessionId(),
					payOrderEntity.getWechatUserId(),
					payOrderEntity.getId());

			payOrderEntity.setPrizeId(prizeIds);


			return payOrderEntity;

		} finally {

			if (lock.isLocked())
				lock.unlock();

		}


	}

	/**
	 * 检查是否存在隐藏奖品 存在就再次补充普通奖品
	 * @param prizeIds
	 * @param hide
	 * @param prizeInventory
	 */
	private  List<Inventory> checkSupplements(List<Long> prizeIds, List<PrizeInfoEntity> hide, List<Inventory> prizeInventory) {
		if (CollectionUtils.isEmpty(hide))
			return prizeInventory;

		 var containsIds= hide.stream()
				.map(PrizeInfoEntity::getId)
				.collect(Collectors.toList());

		 var contains = prizeIds.stream()
				.filter(containsIds::contains)
				.collect(Collectors.toList());

		 if (CollectionUtils.isEmpty(contains))
			 return prizeInventory;

		for (int i = 0; i < contains.size(); i++) {

			//当前库存
			var currentInventory = prizeInventory
					.stream()
					.map(Inventory::getInventory)
					.reduce(0, Integer::sum);


			var prizeId = this. getaPrizeId(null, null, prizeInventory, currentInventory);

			prizeInventory = prizeInventory
					.stream()
					.map(entry -> {
						if (Objects.equals(entry.getPrizeId(), prizeId))
							entry.setInventory(entry.getInventory() - 1);
						return entry;
					})
					.collect(Collectors.toList());

			prizeIds.add(prizeId);

		}
		return prizeInventory;

	}

	/**
	 * 根据奖品id获取奖品
	 */
	public List<SuccessProducts> getPrizeInfoByNum(List<Long> prizeIds) {

		var prizeInfoEntityList = prizeInfoService.findByIds(prizeIds);

		var gradeIds = prizeInfoEntityList.stream()
				.map(PrizeInfoEntity::getGradeId)
				.collect(Collectors.toList());

		var gradeGroupName = gradeService.findByIds(gradeIds)
				.stream()
				.collect(Collectors.toMap(GradeEntity::getId, GradeEntity::getName));

		var prizeInfoEntityMap = prizeInfoEntityList
				.stream()
				.collect(Collectors.toMap(PrizeInfoEntity::getId, Function.identity()));
		// 统计每个 prizeId 出现的次数
		return prizeIds.stream()
				.collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
				.entrySet()
				.stream()
				.map(entry -> SuccessProducts.getInstance(
						prizeInfoEntityMap.get(entry.getKey()),
						gradeGroupName.get(prizeInfoEntityMap.get(entry.getKey()).getGradeId()),
						entry.getValue()))
				.collect(Collectors.toList());


	}


	/**
	 * @param hide
	 * @param gradeEntityMap
	 * @param prizeInventory
	 * @param commonInventory
	 * @return
	 */

	private Long getaPrizeId(List<PrizeInfoEntity> hide, Map<Long, GradeEntity> gradeEntityMap, List<Inventory> prizeInventory, Integer commonInventory) {

		Map<Long, BigDecimal> probability = new HashMap<>();

		if (!CollectionUtils.isEmpty(hide)&&!CollectionUtils.isEmpty(gradeEntityMap)) {

			var hideMap = hide
					.stream()
					.collect(Collectors.toMap(
							PrizeInfoEntity::getId,
							entry -> gradeEntityMap.get(entry.getGradeId()).getProbability()
					));
			probability.putAll(hideMap);


		}

		var normal = prizeInventory
				.stream()
				.collect(Collectors.toMap(
						Inventory::getPrizeId,
						entry -> {
							var inventory = entry.getInventory();
							if (inventory <= 0)
								return BigDecimal.ZERO;
							return BigDecimal.valueOf(inventory)
									.divide(BigDecimal.valueOf(commonInventory), 3, RoundingMode.HALF_UP);
						}
				));
		probability.putAll(normal);


		return this.selectProduct(probability);

	}


	/**
	 * 更据概率图获取商品
	 *
	 * @param probabilityMap 商品和商品的概率
	 * @return
	 */
	public Long selectProduct(Map<Long, BigDecimal> probabilityMap) {
		if (probabilityMap.isEmpty()) {
			throw new IllegalArgumentException("Probability map is empty");
		}

		// 计算总和和最大小数位数
		BigDecimal total = BigDecimal.ZERO;
		int maxScale = 0;
		for (BigDecimal value : probabilityMap.values()) {
			if (value.compareTo(BigDecimal.ZERO) < 0) {
				throw new IllegalArgumentException("Negative probability found: " + value);
			}
			total = total.add(value);
			maxScale = Math.max(maxScale, value.scale());
		}

		if (total.compareTo(BigDecimal.ZERO) <= 0) {
			throw new IllegalArgumentException("Total probability must be positive");
		}

		// 缩放因子
		BigDecimal factor = BigDecimal.TEN.pow(maxScale);
		List<Map.Entry<Long, Long>> scaledEntries = new ArrayList<>();
		long totalScaled = 0;

		// 转换为整数权重
		for (Map.Entry<Long, BigDecimal> entry : probabilityMap.entrySet()) {
			BigDecimal scaledValue = entry.getValue().setScale(maxScale, RoundingMode.HALF_UP);
			BigDecimal weightBd = scaledValue.multiply(factor);
			try {
				long weight = weightBd.longValueExact();
				scaledEntries.add(new AbstractMap.SimpleEntry<>(entry.getKey(), weight));
				totalScaled += weight;
			} catch (ArithmeticException e) {
				throw new IllegalArgumentException("Value " + entry.getValue() + " cannot be scaled to an exact integer.", e);
			}
		}

		if (totalScaled <= 0) {
			throw new IllegalArgumentException("Sum of scaled weights must be positive");
		}

		// 生成随机数
		long random = ThreadLocalRandom.current().nextLong(totalScaled);

		// 累加权重选择商品
		long accumulated = 0;
		for (Map.Entry<Long, Long> entry : scaledEntries) {
			accumulated += entry.getValue();
			if (accumulated > random) {
				return entry.getKey();
			}
		}

		return scaledEntries.get(scaledEntries.size() - 1).getKey(); // 理论上不会执行到这里
	}

	public Sales sales(Long topicId) {
		var seriesTopicEntity = seriesTopicService.findById(topicId);
		if (Objects.isNull(seriesTopicEntity))
			return new Sales();

		var prizeInfoEntities = prizeInfoService.findByTopicId(topicId);

		if (CollectionUtils.isEmpty(prizeInfoEntities))
			return new Sales();
		//获取商品
		var prizeMapPrice = prizeInfoEntities.stream()
				.collect(Collectors.toMap(PrizeInfoEntity::getId, PrizeInfoEntity::getPrice));

		//获取订单
		var orderEntityList = orderService.findByTopicId(topicId);

		if (CollectionUtils.isEmpty(orderEntityList))
			return new Sales();
		//成本
		var costPrice = orderEntityList
				.stream()
				.map(orderEntity ->
						orderEntity.getOrderPrizeEntities()
								.stream()
								.map(orderPrizeEntity -> prizeMapPrice.get(orderPrizeEntity.getProductId()))
								.reduce(BigDecimal.ZERO, BigDecimal::add)

				)
				.reduce(BigDecimal.ZERO, BigDecimal::add);
		//销售单量
		var orderNumber = orderEntityList.size();
		//销售金额
		var salesAmount = BigDecimal.valueOf(orderNumber).multiply(seriesTopicEntity.getPrice());
		//实际利润
		var actualProfit = salesAmount.subtract(costPrice);


		return Sales.builder()
				.salesAmount(salesAmount)
				.productTotalValue(costPrice)
				.orderNumber(orderNumber)
				.topicName(seriesTopicEntity.getName())
				.actualProfit(actualProfit)
				.build();
	}

	public List<PrizePublicity> prizePublicity(Integer gradeType, Long topicId) {

		var gradeEntities = gradeService.findByList(GradeEntity.builder().type(gradeType).build());

		if (CollectionUtils.isEmpty(gradeEntities))
			return Collections.emptyList();

		var gradeIds = gradeEntities.stream()
				.map(GradeEntity::getId)
				.collect(Collectors.toList());

		var prizeInfoEntities = prizeInfoService.findByGradeIds(gradeIds);

		if (CollectionUtils.isEmpty(prizeInfoEntities))
			return Collections.emptyList();

		var prizeInfoIds = prizeInfoEntities
				.stream()
				.filter(s -> {
					if (Objects.isNull(topicId))
						return Objects.equals(s.getTopicId(), topicId);
					return true;
				})
				.map(PrizeInfoEntity::getId)
				.collect(Collectors.toList());

		var topicIds = prizeInfoEntities.stream()
				.map(PrizeInfoEntity::getTopicId)
				.collect(Collectors.toList());

		var prizeInfoEntityMap = prizeInfoEntities.stream()
				.collect(Collectors.toMap(PrizeInfoEntity::getId, Function.identity()));

		var seriesTopicMap = seriesTopicService.findByIds(topicIds)
				.stream()
				.collect(Collectors.toMap(SeriesTopicEntity::getId, Function.identity()));


		var orderPrizeEntities = orderService.findByPrizeIds(prizeInfoIds);

		if (CollectionUtils.isEmpty(orderPrizeEntities))
			return Collections.emptyList();

		var wechatUserIds = orderPrizeEntities.stream()
				.map(OrderPrizeEntity::getWechatUserId)
				.collect(Collectors.toList());

		var wechatUserMap = wechatUserService.getByIds(wechatUserIds)
				.stream()
				.collect(Collectors.toMap(WechatUserEntity::getId, Function.identity()));

		return orderPrizeEntities
				.stream()
				.map(s -> {
					var wechatUserEntity = wechatUserMap.get(s.getWechatUserId());
					var prizeInfoEntity = prizeInfoEntityMap.get(s.getProductId());
					return PrizePublicity.builder()
							.name(Strings.isBlank(wechatUserEntity.getName()) ? "用户-".concat(wechatUserEntity.getPhone().substring(0, 3)).concat("***") : wechatUserEntity.getName())
							.avatar(wechatUserEntity.getAvatar())
							.seriesName(seriesTopicMap.get(prizeInfoEntity.getTopicId()).getName())
							.prizeName(prizeInfoEntity.getPrizeName())
							.time(s.getCreateTime())
							.build();
				}).collect(Collectors.toList());

	}


	/**
	 * 盒柜
	 */
	public List<PrizeInfoEntity> boxCabinets(Long wechatUserId) {
		var orderPrizeEntities = orderService.findByWechatUserId(wechatUserId);

		if (CollectionUtils.isEmpty(orderPrizeEntities))
			return Collections.emptyList();

		var orderPrizeMap = orderPrizeEntities.stream()
				.collect(Collectors.groupingBy(OrderPrizeEntity::getProductId));

		var productIds = orderPrizeEntities.stream()
				.map(OrderPrizeEntity::getProductId)
				.collect(Collectors.toList());

		var prizeInfoEntities = prizeInfoService.findByIds(productIds);
		return prizeInfoEntities.stream()
				.peek(s -> s.setInventory(orderPrizeMap.getOrDefault(s.getId(), List.of()).size()))
				.collect(Collectors.toList());
	}

	@Transactional(rollbackFor = Exception.class)
	public void generateLogisticsOrder(LogisticsOrder logisticsOrder) {

		//订单
		var orderPrizeEntities = orderService.deductionInventory(logisticsOrder.getGoods());
		logisticsOrderService.generateLogisticsOrder(logisticsOrder.getAddressId(), orderPrizeEntities, logisticsOrder.getWechatUserId());


	}

	public List<WechatOrderList> getPrizeInfo(Long wechatUserId) {

		if (Objects.isNull(wechatUserId))
			return Collections.emptyList();

		var order = OrderEntity.builder().wechatUserId(wechatUserId).build();
		var orderEntities = orderService.list(order);
		if (CollectionUtils.isEmpty(orderEntities))
			return List.of();

		var orderPrizeEntities = orderService.findByWechatUserId(wechatUserId);


		var topicIdIds = orderEntities
				.stream()
				.map(OrderEntity::getTopicId)
				.distinct()
				.collect(Collectors.toList());

		var seriesTopicNameMap = seriesTopicService.findByIds(topicIdIds).stream()
				.collect(Collectors.toMap(SeriesTopicEntity::getId, SeriesTopicEntity::getName));

		var productIds = orderPrizeEntities.stream()
				.map(OrderPrizeEntity::getProductId)
				.distinct()
				.collect(Collectors.toList());

		var prizeInfoMap = prizeInfoService.findByIds(productIds)
				.stream()
				.collect(Collectors.toMap(PrizeInfoEntity::getId, Function.identity()));


		Map<Long, Map<Long, List<OrderPrizeEntity>>> orderPrizeMap = orderPrizeEntities.stream()
				.collect(Collectors.groupingBy(OrderPrizeEntity::getOrderId, Collectors.groupingBy(OrderPrizeEntity::getProductId)));

		return orderEntities.stream()
				.map(s ->
						WechatOrderList
								.builder()
								.id(s.getId())
								.topicId(s.getTopicId())
								.seriesName(seriesTopicNameMap.get(s.getTopicId()))
								.createTime(s.getCreateTime())
								.status(s.getStatus())
								.goods(
										orderPrizeMap.getOrDefault(s.getId(), Map.of())
												.entrySet()
												.stream()
												.map(s1 -> {
													var prizeInfoEntity = prizeInfoMap.getOrDefault(s1.getKey(), PrizeInfoEntity.builder().build());

													return PrizeInfoEntity.builder()
															.id(prizeInfoEntity.getId())
															.type(prizeInfoEntity.getType())
															.prizeUrl(prizeInfoEntity.getPrizeUrl())
															.prizeName(prizeInfoEntity.getPrizeName())
															.inventory(CollectionUtils.isEmpty(s1.getValue()) ? 0 : s1.getValue().size())
															.build();

												}).collect(Collectors.toList())
								)
								.build()

				)
				.collect(Collectors.toList());

	}
}
