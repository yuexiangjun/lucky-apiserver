package com.lucky.infrastructure.repository.mysql.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lucky.infrastructure.repository.mysql.po.LogisticsOrderPrizePO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 物流订单关联商品
 */
@Mapper
public interface LogisticsOrderPrizeMapper extends BaseMapper<LogisticsOrderPrizePO> {

}
