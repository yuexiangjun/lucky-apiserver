package com.lucky.infrastructure.repository.mysql.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lucky.infrastructure.repository.mysql.po.LogisticsOrderPO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 物流订单
 */
@Mapper

public interface LogisticsOrderMapper extends BaseMapper<LogisticsOrderPO> {

}
