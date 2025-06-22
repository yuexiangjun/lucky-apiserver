package com.lucky.infrastructure.repository;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lucky.domain.entity.DeliveryAddressEntity;
import com.lucky.domain.repository.DeliveryAddressRepository;
import com.lucky.infrastructure.repository.mysql.mapper.DeliveryAddressMapper;
import com.lucky.infrastructure.repository.mysql.po.DeliveryAddressPO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 收货地址信息
 */
@Component
public class DeliveryAddressRepositoryImpl extends ServiceImpl<DeliveryAddressMapper, DeliveryAddressPO> implements DeliveryAddressRepository {

    @Override
    public Long saveOrUpdate(DeliveryAddressEntity entity) {
        var po = DeliveryAddressPO.getInstance(entity);
        this.saveOrUpdate(po);
        return po.getId();
    }

    @Override
    public DeliveryAddressEntity getById(Long id) {
        var eq = Wrappers.lambdaQuery(DeliveryAddressPO.class)
                .eq(DeliveryAddressPO::getId, id);
        return DeliveryAddressPO.toEntity(this.getOne(eq));
    }

    @Override
    public Boolean deleteById(Long id) {
        int i = this.baseMapper.deleteById(id);
        return i > 0 ? true : false;
    }

    @Override
    public List<DeliveryAddressEntity> getByWechatUserId(Long wechatUserId) {
        var eq = Wrappers.lambdaQuery(DeliveryAddressPO.class)
                .eq(DeliveryAddressPO::getWechatUserId, wechatUserId)
                .orderByDesc(DeliveryAddressPO::getIsDefault)
                .orderByDesc(DeliveryAddressPO::getId);
        return this.list(eq).stream()
                .map(DeliveryAddressPO::toEntity)
                .collect(Collectors.toList());
    }

    @Override
    public Boolean saveOrUpdateBatch(List<DeliveryAddressEntity> entityList) {
        if (CollectionUtils.isEmpty(entityList))
            return false;
        var deliveryAddressPOS = entityList.stream()
                .map(DeliveryAddressPO::getInstance)
                .collect(Collectors.toList());

        return this.saveOrUpdateBatch(deliveryAddressPOS);

    }
}
