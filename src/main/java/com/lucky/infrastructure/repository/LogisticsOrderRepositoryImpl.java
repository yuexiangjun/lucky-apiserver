package com.lucky.infrastructure.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lucky.domain.entity.LogisticsOrderEntity;
import com.lucky.domain.repository.LogisticsOrderRepository;
import com.lucky.infrastructure.repository.mysql.mapper.LogisticsOrderMapper;
import com.lucky.infrastructure.repository.mysql.po.LogisticsOrderPO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 物流订单
 */

@Component
public class LogisticsOrderRepositoryImpl extends ServiceImpl<LogisticsOrderMapper, LogisticsOrderPO> implements LogisticsOrderRepository {

    @Override
    public Long saveOrUpdate(LogisticsOrderEntity logisticsOrderEntity) {

        var bean = LogisticsOrderPO.getInstance(logisticsOrderEntity);
        this.saveOrUpdate(bean);
        return bean.getId();

    }

    @Override
    public List<LogisticsOrderEntity> getByWechatUserId(Long wechatUserId) {
        var eq = Wrappers.lambdaQuery(LogisticsOrderPO.class)
                .eq(LogisticsOrderPO::getWechatUserId, wechatUserId)
                .orderByAsc(LogisticsOrderPO::getStatus)
                .orderByDesc(LogisticsOrderPO::getCreateTime);
        return
                this.list(eq)
                        .stream()
                        .map(LogisticsOrderPO::toEntity)
                        .collect(Collectors.toList());


    }

    @Override
    public List<LogisticsOrderEntity> getByAdminList(LogisticsOrderEntity entity) {
        var queryWrapper = new LambdaQueryWrapper<LogisticsOrderPO>()
                .eq(Objects.nonNull(entity.getStatus()), LogisticsOrderPO::getStatus, entity.getStatus())
                .eq(Objects.nonNull(entity.getWechatUserId()), LogisticsOrderPO::getWechatUserId, entity.getWechatUserId())
                .like(Objects.nonNull(entity.getNumber()), LogisticsOrderPO::getNumber, entity.getNumber())
                .like(Objects.nonNull(entity.getLogisticsNumber()), LogisticsOrderPO::getLogisticsNumber, entity.getLogisticsNumber());
        return
                this.list(queryWrapper)
                        .stream()
                        .map(LogisticsOrderPO::toEntity)
                        .collect(Collectors.toList());
    }
}
