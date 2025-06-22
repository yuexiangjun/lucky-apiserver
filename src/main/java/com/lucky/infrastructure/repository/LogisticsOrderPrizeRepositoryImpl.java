package com.lucky.infrastructure.repository;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lucky.domain.entity.LogisticsOrderPrizeEntity;
import com.lucky.domain.repository.LogisticsOrderPrizeRepository;
import com.lucky.infrastructure.repository.mysql.mapper.LogisticsOrderPrizeMapper;
import com.lucky.infrastructure.repository.mysql.po.LogisticsOrderPrizePO;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 物流订单关联商品
 */
@Component
public class LogisticsOrderPrizeRepositoryImpl extends ServiceImpl<LogisticsOrderPrizeMapper, LogisticsOrderPrizePO> implements LogisticsOrderPrizeRepository {

    @Override
    public  Boolean savaOrUpdateBatch(List<LogisticsOrderPrizeEntity> logisticsOrderPrizes) {
         var logisticsOrderPrizePOS= logisticsOrderPrizes.stream()
                .map(LogisticsOrderPrizePO::getInstance)
                .collect(Collectors.toList());
       return   this.saveOrUpdateBatch(logisticsOrderPrizePOS);


    }

    @Override
    public List<LogisticsOrderPrizeEntity> getByLogisticsOrderIds(List<Long> logisticsOrderIds) {
        if (CollectionUtils.isEmpty(logisticsOrderIds))
        return List.of();
        var wrapper = Wrappers.<LogisticsOrderPrizePO>lambdaQuery()
                .in(LogisticsOrderPrizePO::getLogisticsOrderId, logisticsOrderIds);
        return this.list(wrapper)
                .stream()
                .map(LogisticsOrderPrizePO::toEntity)
                .collect(Collectors.toList());
    }
}
