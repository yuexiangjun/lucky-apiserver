package com.lucky.application;

import com.lucky.domain.GradeService;
import com.lucky.domain.PrizeInfoService;
import com.lucky.domain.RedisService;
import com.lucky.domain.SessionInfoService;
import com.lucky.domain.entity.GradeEntity;
import com.lucky.domain.entity.PrizeInfoEntity;
import com.lucky.domain.entity.SessionInfoEntity;
import com.lucky.domain.valueobject.BaseDataPage;
import com.lucky.domain.valueobject.InventoryInfo;
import com.lucky.domain.valueobject.SessionInfo;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class SessionInfoServer {
	private final SessionInfoService sessionInfoService;
	private final PrizeInfoService prizeInfoService;
	private final GradeService gradeService;
	private final RedisService redisService;

	public SessionInfoServer(SessionInfoService sessionInfoService,
	                         PrizeInfoService prizeInfoService,
	                         GradeService gradeService, RedisService redisService) {
		this.sessionInfoService = sessionInfoService;
		this.prizeInfoService = prizeInfoService;
		this.gradeService = gradeService;
		this.redisService = redisService;
	}

    /**
     * 商品详情
     */
    public BaseDataPage<SessionInfo> findByTopicIdPageNO(Long topicId, Integer page, Integer size, Long wechatUserId) {

        var sessionInfoEntityPage = sessionInfoService.findByTopicIdPageNO(topicId, page, size);


        BaseDataPage<SessionInfo> sessionInfoBaseDataPage = this.getSessionInfoBaseDataPage(topicId, sessionInfoEntityPage, wechatUserId);

        return sessionInfoBaseDataPage;

    }

    /**
     * 全部场次
     */
    public BaseDataPage<SessionInfo> findByTopicIdPageStatus(Long topicId, Integer page, Integer size, Long wechatUserId) {
        var sessionInfoEntityPage = sessionInfoService.findByTopicIdPageStatus(topicId, page, size);

        return this.getSessionInfoBaseDataPage(topicId, sessionInfoEntityPage, wechatUserId);
    }

    private BaseDataPage<SessionInfo> getSessionInfoBaseDataPage(Long topicId,
                                                                 BaseDataPage<SessionInfoEntity> sessionInfoEntityPage,
                                                                 Long wechatUserId) {
        var dataList = sessionInfoEntityPage.getDataList();

        if (CollectionUtils.isEmpty(dataList))
            return new BaseDataPage<>(0l);
        //更据系列获取奖品
        var prizeInfoEntities = prizeInfoService.findByTopicId(topicId);

        var gradeIds = prizeInfoEntities.stream()
                .map(PrizeInfoEntity::getGradeId)
                .collect(Collectors.toList());
        var gradeEntityMap = this.getGradeEntityMap(gradeIds);
        var hides = prizeInfoEntities.stream()
                .filter(p -> Objects.equals(p.getType(), 1))
                .map(s -> {
                            GradeEntity gradeEntity = gradeEntityMap.get(s.getGradeId());

                            var probability = Objects.isNull(gradeEntity.getProbability()) ? BigDecimal.ZERO : gradeEntity.getProbability().multiply(BigDecimal.valueOf(1000));

                            return InventoryInfo.builder()
                                    .prizeId(s.getId())
                                    .prizeUrl(s.getPrizeUrl())
                                    .prizeName(s.getPrizeName())
                                    .gradeName(gradeEntity.getName())
                                    .sort(gradeEntity.getSort())
                                    .probability(probability.toPlainString().concat("‰"))
                                    .build();
                        }
                )
                .collect(Collectors.toList());


        var prizeInfoEntityMap = this.getPrizeInfoEntityMap(prizeInfoEntities);
        //总库存
        var totalInventory = prizeInfoEntities.stream()
                .filter(p -> p.getType() == 2)
                .map(PrizeInfoEntity::getInventory)
                .reduce(0, Integer::sum);

        var totalInventoryMap = prizeInfoEntities.stream()
                .collect(Collectors.toMap(PrizeInfoEntity::getId, PrizeInfoEntity::getInventory));


        var sessionInfoList = dataList.stream()
                .map(s -> {
                    Integer remainInventory = s.totalInventory();

                    var inventoryInfos = s.getPrizeInventory()
                            .stream()
                            .map(p -> {

								PrizeInfoEntity prizeInfoEntity = prizeInfoEntityMap.get(p.getPrizeId());
								GradeEntity gradeEntity = gradeEntityMap.get(prizeInfoEntity.getGradeId());
								var probability = new BigDecimal(0);
								if (new BigDecimal(p.getInventory()).compareTo(BigDecimal.ZERO) > 0)
									probability = new BigDecimal(p.getInventory()).divide(new BigDecimal(remainInventory), 2, BigDecimal.ROUND_HALF_UP).multiply(BigDecimal.valueOf(100));
								return InventoryInfo.builder()
										.prizeId(p.getPrizeId())
										.totalInventory(totalInventoryMap.get(p.getPrizeId()))
										.remainInventory(p.getInventory())
										.prizeUrl(prizeInfoEntity.getPrizeUrl())
										.prizeName(prizeInfoEntity.getPrizeName())
										.gradeName(gradeEntity.getName())
										.probability(probability.toPlainString().concat("%"))
										.sort(gradeEntity.getSort())
										.build();
							})
							.collect(Collectors.toList());

                    if (!CollectionUtils.isEmpty(hides)) {
                        inventoryInfos.addAll(hides);
                    }
                    inventoryInfos.sort(Comparator.comparingInt(InventoryInfo::getSort));

                    long expire = redisService.getExpire(getByBuyKey(topicId, s.getId()));

                    Boolean isLineUpSuccess = false;
                    if (Objects.nonNull(wechatUserId)) {
                        Object cacheObject = redisService.getCacheObject(getByBuyKey(topicId, s.getId()));
                        if (Objects.nonNull(cacheObject))
                            if (Objects.equals(String.valueOf(cacheObject), String.valueOf(wechatUserId)))
                                isLineUpSuccess = true;


                    }


                    return SessionInfo.builder()
                            .id(s.getId())
                            .totalInventory(totalInventory)
                            .sessionNumber(s.getSessionNumber())
                            .remainInventory(remainInventory)
                            .inventoryInfos(inventoryInfos)
                            .endTime(expire >= 0 ? expire : null)
		                    .status(s.getStatus())
                            .isLineUpSuccess(isLineUpSuccess)
                            .build();


                })

                .collect(Collectors.toList());


		return BaseDataPage.newInstance(
				sessionInfoEntityPage.getTotal(),
				sessionInfoEntityPage.getPages(),
				sessionInfoList);
	}

    /**
     * 奖项详情
     *
     * @param gradeIds
     * @return
     */
    private Map<Long, GradeEntity> getGradeEntityMap(List<Long> gradeIds) {
        return gradeService.findByIds(gradeIds)
                .stream()
                .collect(Collectors.toMap(GradeEntity::getId, Function.identity(), (v1, v2) -> v1));
    }

    /**
     * 奖品详情
     *
     * @param prizeInfoEntities
     * @return
     */

    private Map<Long, PrizeInfoEntity> getPrizeInfoEntityMap(List<PrizeInfoEntity> prizeInfoEntities) {
        return prizeInfoEntities.stream()
                .collect(Collectors.toMap(PrizeInfoEntity::getId, Function.identity(), (v1, v2) -> v1));
    }

    private static String getByBuyKey(Long topicId, Long sessionId) {
        var key = "PAY_LOCK_NAME:".concat("buy:")
                .concat(String.valueOf(topicId))
                .concat(":")
                .concat(String.valueOf(sessionId));
        return key;
    }
}
