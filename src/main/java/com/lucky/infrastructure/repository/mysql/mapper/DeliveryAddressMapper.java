package com.lucky.infrastructure.repository.mysql.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lucky.infrastructure.repository.mysql.po.DeliveryAddressPO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 收货地址信息
 */
@Mapper
public interface DeliveryAddressMapper extends BaseMapper<DeliveryAddressPO> {

}
