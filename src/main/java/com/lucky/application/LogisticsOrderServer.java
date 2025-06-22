package com.lucky.application;

import cn.hutool.core.collection.CollectionUtil;
import com.lucky.domain.LogisticsOrderService;
import com.lucky.domain.PrizeInfoService;
import com.lucky.domain.entity.LogisticsOrderEntity;
import com.lucky.domain.entity.LogisticsOrderPrizeEntity;
import com.lucky.domain.entity.PrizeInfoEntity;
import com.lucky.domain.valueobject.LogisticsOrderInfo;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class LogisticsOrderServer {
    private final LogisticsOrderService logisticsOrderService;
    private final PrizeInfoService prizeInfoService;

    public LogisticsOrderServer(LogisticsOrderService logisticsOrderService, PrizeInfoService prizeInfoService) {
        this.logisticsOrderService = logisticsOrderService;
        this.prizeInfoService = prizeInfoService;
    }


    /**
     * 查询物流订单
     */
    public List<LogisticsOrderInfo> getByWechatUserId(Long wechatUserId) {

        var logisticsOrderEntities = logisticsOrderService.getByWechatUserId(wechatUserId);

        return this.getLogisticsOrderInfos(logisticsOrderEntities);


    }

    private List<LogisticsOrderInfo> getLogisticsOrderInfos(List<LogisticsOrderEntity> logisticsOrderEntities) {

        if (CollectionUtil.isEmpty(logisticsOrderEntities))
            return List.of();

        var productIds = logisticsOrderEntities.stream()
                .map(s -> s.getGoods()
                        .stream()
                        .map(LogisticsOrderPrizeEntity::getProductId)
                        .collect(Collectors.toList())
                )
                .filter(CollectionUtil::isNotEmpty)
                .flatMap(List::stream)
                .collect(Collectors.toSet());

        var productIdList = productIds.stream().collect(Collectors.toList());

        var prizeInfoMap = prizeInfoService.findByIds(productIdList)
                .stream()
                .collect(Collectors.toMap(PrizeInfoEntity::getId, Function.identity()));


        return logisticsOrderEntities
                .stream()
                .map(s -> {
                    var prizeInfoEntities = s.getGoods()
                            .stream()
                            .collect(Collectors.groupingBy(LogisticsOrderPrizeEntity::getProductId))
                            .entrySet()
                            .stream()
                            .map(s1 -> {

                                var prizeInfoEntity = prizeInfoMap.get(s1.getKey());

                                List<LogisticsOrderPrizeEntity> value = s1.getValue();
                                prizeInfoEntity.setInventory(CollectionUtil.isEmpty(value) ? 0 : value.size());
                                return prizeInfoEntity;
                            }).collect(Collectors.toList());

                    return LogisticsOrderInfo.builder()
                            .id(s.getId())
                            .status(s.getStatus())
                            .number(s.getNumber())
                            .logisticsNumber(s.getLogisticsNumber())
                            .logisticsCompany(s.getLogisticsCompany())
                            .createTime(s.getCreateTime())
                            .goods(prizeInfoEntities)
                            .build();
                }).collect(Collectors.toList());
    }

    /**
     * 后台查询物流订单
     */
    public List<LogisticsOrderInfo> getByAdminList(LogisticsOrderEntity entity) {

        var logisticsOrderEntities = logisticsOrderService.getByAdminList(entity);


        return this.getLogisticsOrderInfos(logisticsOrderEntities);
    }


    /**
     * 修改物流订单
     */
    public void updateLogisticsOrder(LogisticsOrderEntity logisticsOrderEntity) {

        logisticsOrderService.updateLogisticsOrder(logisticsOrderEntity);
    }


}
