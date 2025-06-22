package com.lucky.infrastructure.repository;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lucky.domain.entity.CustomerInfoEntity;
import com.lucky.domain.repository.CustomerInfoRepository;
import com.lucky.infrastructure.repository.mysql.mapper.CustomerInfoMapper;
import com.lucky.infrastructure.repository.mysql.po.CustomerInfoPO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 客服信息
 */

@Component
public class CustomerInfoRepositoryImpl extends ServiceImpl<CustomerInfoMapper, CustomerInfoPO> implements CustomerInfoRepository {


    @Override
    public Long saveOrUpdate(CustomerInfoEntity entity) {
        if (Objects.isNull(entity)) {
            return null;
        }
        var customerInfoPO = CustomerInfoPO.getInstance(entity);
        this.saveOrUpdate(customerInfoPO);
        return customerInfoPO.getId();
    }

    @Override
    public List<CustomerInfoEntity> getList() {
        var customerInfoPOLambdaQueryWrapper = Wrappers.lambdaQuery(CustomerInfoPO.class);

        return list(customerInfoPOLambdaQueryWrapper)
                .stream()
                .map(CustomerInfoPO::toEntity)
                .collect(Collectors.toList());
    }
}
