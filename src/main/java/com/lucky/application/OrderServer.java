package com.lucky.application;

import cn.hutool.core.collection.CollectionUtil;
import com.lucky.application.tripartite.WeChatPayServer;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
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

	private final PayOrderService payOrderService;

	private final RedissionConfig redissionConfig;
	private final static String DEDUCTION = "DEDUCTION:";
	private final WeChatPayServer weChatPayServer;

	private final SystemConfigService systemConfigService;

	public OrderServer(OrderService orderService,
	                   PrizeInfoService prizeInfoService,
	                   GradeService gradeService,
	                   SeriesTopicService seriesTopicService,
	                   WechatUserService wechatUserService, SessionInfoService sessionInfoService, LogisticsOrderService logisticsOrderService, PayOrderService payOrderService, RedissionConfig redissionConfig, WeChatPayServer weChatPayServer, SystemConfigService systemConfigService) {
		this.orderService = orderService;
		this.prizeInfoService = prizeInfoService;
		this.gradeService = gradeService;
		this.seriesTopicService = seriesTopicService;
		this.wechatUserService = wechatUserService;
		this.sessionInfoService = sessionInfoService;

		this.logisticsOrderService = logisticsOrderService;
		this.payOrderService = payOrderService;

		this.redissionConfig = redissionConfig;
		this.weChatPayServer = weChatPayServer;
		this.systemConfigService = systemConfigService;
	}


	/**
	 * 列表
	 */
	public List<Order> list(OrderEntity entity, String userNameOrPhone, String seriesName) {
		List<Long> findWechatUserIds = new ArrayList<Long>();
		if (Strings.isNotBlank(userNameOrPhone)) {
			var wechatUserEntities = wechatUserService.getUserNameOrPhone(userNameOrPhone);
			findWechatUserIds = wechatUserEntities.stream()
					.map(WechatUserEntity::getId)
					.collect(Collectors.toList());
		}
		//获取系列id
		List<Long> seriesIds = new ArrayList<Long>();
		if (Strings.isNotBlank(userNameOrPhone)) {
			var seriesTopicEntities = seriesTopicService.getIdByName(seriesName);
			seriesIds = seriesTopicEntities.stream()
					.map(SeriesTopicEntity::getId)
					.collect(Collectors.toList());

		}


		var list = orderService.listIds(entity, findWechatUserIds, seriesIds);

		if (CollectionUtils.isEmpty(list))
			return List.of();

		//主题id
		var topicIds = list.stream()
				.map(OrderEntity::getTopicId)
				.collect(Collectors.toList());

		//商品id
		var productIds = list.stream()
				.map(s -> s.getOrderPrizeEntities()
						.stream()
						.map(OrderPrizeEntity::getProductId)
						.collect(Collectors.toList())
				)
				.flatMap(List::stream)
				.collect(Collectors.toList());

		var wechatUserIds = list.stream()
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

		return list.stream()
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
				.collect(Collectors.toList());


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

		//获取到当前用户的消费金额
		var amountSpent = payOrderService.findByWechatUserIdAndPayMoney(payOrderEntity.getWechatUserId());
		//获取系列的单价
		var seriesTopicEntity = seriesTopicService.findById(payOrderEntity.getTopicId());


//		RLock lock = redissionConfig.redissonClient().getLock(DEDUCTION + payOrderEntity.getId());
//		lock.lock();
//		try {
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

				amountSpent = amountSpent.add(seriesTopicEntity.getPrice());

				Long prizeId;

				//不能参加的奖品
				var noPrizeIds = this.getNoParticipatePrize(gradeIds, amountSpent, prizeInfoEntities);
				if (!CollectionUtils.isEmpty(noPrizeIds)) {

					var prizeInventories = prizeInventory
							.stream()
							.filter(entry -> !noPrizeIds.contains(entry.getPrizeId()))
							.collect(Collectors.toList());

					var hides = hide
							.stream()
							.filter(entry -> !noPrizeIds.contains(entry.getId()))
							.collect(Collectors.toList());

					prizeId = this.getaPrizeId(hides, gradeEntityMap, prizeInventories);

				} else {
					prizeId = this.getaPrizeId(hide, gradeEntityMap, prizeInventory);
				}

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
			//不能参加的奖品
			var noPrizeIds = this.getNoParticipatePrize(gradeIds, amountSpent, prizeInfoEntities);

			if (!CollectionUtils.isEmpty(noPrizeIds)) {
				var prizeInventories = prizeInventory
						.stream()
						.filter(entry -> !noPrizeIds.contains(entry.getPrizeId()))
						.collect(Collectors.toList());


				prizeInventory = this.checkSupplements(prizeIds, hide, prizeInventories);

			} else {
				prizeInventory = this.checkSupplements(prizeIds, hide, prizeInventory);
			}


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

//		} finally {
//
//			if (lock.isLocked())
//				lock.unlock();
//
//		}


	}

	/**
	 * 获取不能参与的奖项
	 */
	private List<Long> getNoParticipatePrize(List<Long> gradeIds, BigDecimal amountSpent, List<PrizeInfoEntity> prizeInfoEntities) {

		var systemConfigEntities = systemConfigService.findByGradeIds(gradeIds);

		if (CollectionUtils.isEmpty(systemConfigEntities))
			return List.of();

		var noParticipatePrize = systemConfigEntities.stream()
				.filter(s -> amountSpent.compareTo(s.getMinConsume()) < 0)
				.map(SystemConfigEntity::getGradeId)
				.collect(Collectors.toList());

		return prizeInfoEntities.stream()
				.filter(s -> noParticipatePrize.contains(s.getGradeId()))
				.map(PrizeInfoEntity::getId)
				.collect(Collectors.toList());


	}


	/**
	 * 检查是否存在隐藏奖品 存在就再次补充普通奖品
	 *
	 * @param prizeIds
	 * @param hide
	 * @param prizeInventory
	 */
	private List<Inventory> checkSupplements(List<Long> prizeIds, List<PrizeInfoEntity> hide, List<Inventory> prizeInventory) {
		if (CollectionUtils.isEmpty(hide))
			return prizeInventory;

		var containsIds = hide.stream()
				.map(PrizeInfoEntity::getId)
				.collect(Collectors.toList());

		var contains = prizeIds.stream()
				.filter(containsIds::contains)
				.collect(Collectors.toList());

		if (CollectionUtils.isEmpty(contains))
			return prizeInventory;

		for (int i = 0; i < contains.size(); i++) {


			var prizeId = this.getaPrizeId(null, null, prizeInventory);

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
	 * @return
	 */

	private Long getaPrizeId(List<PrizeInfoEntity> hide, Map<Long, GradeEntity> gradeEntityMap, List<Inventory> prizeInventory) {


		//当前库存
		var currentInventory = prizeInventory
				.stream()
				.map(Inventory::getInventory)
				.reduce(0, Integer::sum);

		Map<Long, BigDecimal> probability = new HashMap<>();

		if (!CollectionUtils.isEmpty(hide) && !CollectionUtils.isEmpty(gradeEntityMap)) {

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
									.divide(BigDecimal.valueOf(currentInventory), 3, RoundingMode.HALF_UP);
						}
				));
		probability.putAll(normal);
		if (CollectionUtils.isEmpty( probability))
			throw BusinessException.newInstance("没达到消费目标，没有奖品可抽");


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


		//商品总价值
		var costPrice = prizeInfoEntities.stream()
				.map(s -> s.getPrice().multiply(BigDecimal.valueOf(s.getInventory())))
				.reduce(BigDecimal.ZERO, BigDecimal::add).multiply(BigDecimal.valueOf(seriesTopicEntity.getSession()));


		var salesBuilder = Sales.builder()
				.productTotalValue(costPrice)
				.topicName(seriesTopicEntity.getName());

		if (!CollectionUtils.isEmpty(orderEntityList)) {

			//商品销售成本金额
			var costPriceAmount = orderEntityList
					.stream()
					.map(orderEntity ->
							orderEntity.getOrderPrizeEntities()
									.stream()
									.map(orderPrizeEntity -> prizeMapPrice.get(orderPrizeEntity.getProductId()))
									.reduce(BigDecimal.ZERO, BigDecimal::add)

					)
					.reduce(BigDecimal.ZERO, BigDecimal::add);

			//商品销售商品数
			var costPriceNumber = orderEntityList
					.stream()
					.map(orderEntity -> CollectionUtil.isEmpty(orderEntity.getOrderPrizeEntities()) ? 0 : orderEntity.getOrderPrizeEntities().size()

					)
					.reduce(0, Integer::sum);


			//销售单量
			var orderNumber = orderEntityList.size();
			//销售金额
			var salesAmount = BigDecimal.valueOf(costPriceNumber).multiply(seriesTopicEntity.getPrice());
			//实际利润
			var actualProfit = salesAmount.subtract(costPriceAmount);

			return salesBuilder
					.salesAmount(salesAmount)
					.orderNumber(orderNumber)
					.actualProfit(actualProfit)
					.build();
		}
		return salesBuilder.build();


	}

	public List<PrizePublicity> prizePublicity(Integer gradeType, Long topicId) {

		var gradeEntities = gradeService.findByList(GradeEntity.builder().type(1).build());

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
					if (!Objects.isNull(topicId))
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
		var orderPrizeEntities = orderService.findByWechatUserId(wechatUserId, false);

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
		var orderPrizeEntities = orderService.deductionInventory(logisticsOrder.getGoods(), logisticsOrder.getWechatUserId());
		logisticsOrderService.generateLogisticsOrder(logisticsOrder.getAddressId(), orderPrizeEntities, logisticsOrder.getWechatUserId());


	}

	public List<WechatOrderList> getPrizeInfo(Long wechatUserId) {

		if (Objects.isNull(wechatUserId))
			return Collections.emptyList();

		var order = OrderEntity.builder().wechatUserId(wechatUserId).build();
		var orderEntities = orderService.list(order);
		if (CollectionUtils.isEmpty(orderEntities))
			return List.of();

		var orderPrizeEntities = orderService.findByWechatUserId(wechatUserId, null);


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

	public Metrics metrics(LocalDateTime startTime, LocalDateTime endTime) {
		LocalDate now = LocalDate.now();

		//获取用户总数
		var wechatUserEntities = wechatUserService.listByTime(new WechatUserEntity(), startTime, endTime);
		//获取用户总数
		Integer userCount = wechatUserService.count();

		Integer userAddCount = 0;

		if (CollectionUtil.isNotEmpty(wechatUserEntities)) {

			List<WechatUserEntity> collect = wechatUserEntities
					.stream()
					.filter(s -> startTime.toLocalDate().compareTo(now) <= 0 && endTime.toLocalDate().compareTo(now) >= 0)
					.collect(Collectors.toList());
			if (CollectionUtil.isNotEmpty(collect))
				userAddCount = collect.size();
		}


		Integer orderCount = 0;
		BigDecimal orderAmount = BigDecimal.ZERO;
		//获取订单数
		var payOrderEntity = new PayOrderEntity();
		payOrderEntity.setPayStatus(1);
		payOrderEntity.setOrderType(1);
		var payOrderEntities = payOrderService.listByTime(payOrderEntity, startTime, endTime);
		if (CollectionUtil.isNotEmpty(payOrderEntities)) {
			orderCount = payOrderEntities.size();
			orderAmount = payOrderEntities.stream()
					.map(PayOrderEntity::getPayMoney)
					.reduce(BigDecimal.ZERO, BigDecimal::add);
		}
		return Metrics.builder()
				.userCount(userCount)
				.userAddCount(userAddCount)
				.orderCount(orderCount)
				.orderAmount(orderAmount)
				.build();

	}

	public List<ConsumeRank> consumeRank(LocalDateTime startTime, LocalDateTime endTime) {
		//获取订单数
		PayOrderEntity payOrderEntity = new PayOrderEntity();
		payOrderEntity.setPayStatus(1);
		payOrderEntity.setOrderType(1);

		var payOrderEntities = payOrderService.listByTime(payOrderEntity, startTime, endTime);

		if (CollectionUtil.isEmpty(payOrderEntities))
			return Collections.emptyList();
		//用户
		var wechatUserIds = payOrderEntities.stream()
				.map(PayOrderEntity::getWechatUserId)
				.collect(Collectors.toList());

		var wechatUserEntities = wechatUserService.getByIds(wechatUserIds);

		var ownerIds = wechatUserEntities.stream()
				.map(WechatUserEntity::getOwnerId)
				.collect(Collectors.toList());

		var ownerEntities = wechatUserService.getByIds(ownerIds);

		var ownerMap = ownerEntities.stream()
				.collect(Collectors.toMap(WechatUserEntity::getId, Function.identity()));
		var wechatUserMap = wechatUserEntities.stream()
				.collect(Collectors.toMap(WechatUserEntity::getId, Function.identity()));


		return payOrderEntities
				.stream()
				.collect(Collectors.groupingBy(PayOrderEntity::getWechatUserId))
				.entrySet()
				.stream()
				.map(s -> {
					var wechatUserEntity = wechatUserMap.getOrDefault(s.getKey(), new WechatUserEntity());
					String customer = "";
					if (Objects.nonNull(wechatUserEntity)) {
						WechatUserEntity orDefault = ownerMap.getOrDefault(wechatUserEntity.getOwnerId(), new WechatUserEntity());
						customer = Strings.isNotBlank(orDefault.getName()) ? orDefault.getName() : orDefault.getPhone();
					}

					var consumeRankBuilder = ConsumeRank
							.builder()
							.userImage(wechatUserEntity.getAvatar())
							.joinTime(wechatUserEntity.getCreateTime())
							.userName(Strings.isNotBlank(wechatUserEntity.getName()) ? wechatUserEntity.getName() : wechatUserEntity.getPhone())
							.customer(customer);


					List<PayOrderEntity> value = s.getValue();
					if (!CollectionUtils.isEmpty(value)) {
						BigDecimal reduce = value.stream().map(PayOrderEntity::getPayMoney).reduce(BigDecimal.ZERO, BigDecimal::add);
						return consumeRankBuilder.amount(reduce)
								.orderCount(value.size())
								.build();
					} else
						return consumeRankBuilder.amount(BigDecimal.ZERO)
								.orderCount(0)
								.build();

				})
				.sorted((o1, o2) -> o2.getAmount().compareTo(o1.getAmount()))
				.collect(Collectors.toList());


	}


	public void decompose(LogisticsOrder logisticsOrder) {

		//订单
		var orderPrizeEntities = orderService.deductionInventory(logisticsOrder.getGoods(), logisticsOrder.getWechatUserId());

		if (CollectionUtil.isEmpty(orderPrizeEntities))
			return;

		//兑换成福币
		var productIds = orderPrizeEntities.stream()
				.map(OrderPrizeEntity::getProductId)
				.collect(Collectors.toList());
		//获取到商品的价格

		var prizeInfoMap = prizeInfoService.findByIds(productIds)
				.stream()
				.collect(Collectors.toMap(PrizeInfoEntity::getId, Function.identity()));

		var price = orderPrizeEntities
				.stream()
				.map(s -> {
					var prizeInfoEntity = prizeInfoMap.get(s.getProductId());

					if (Objects.nonNull(prizeInfoEntity))
						return prizeInfoEntity.getPrice();
					return BigDecimal.ZERO;


				})
				.reduce(BigDecimal.ZERO, BigDecimal::add);

		wechatUserService.balanceAdd(logisticsOrder.getWechatUserId(), price, 0l, 2);


	}
}
