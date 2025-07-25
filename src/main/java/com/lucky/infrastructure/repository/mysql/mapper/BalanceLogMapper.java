package com.lucky.infrastructure.repository.mysql.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lucky.infrastructure.repository.mysql.po.BalanceLogPO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BalanceLogMapper extends BaseMapper<BalanceLogPO> {

}
