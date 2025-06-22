package com.lucky.infrastructure.repository.mysql.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lucky.infrastructure.repository.mysql.po.PrizeInfoPO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PrizeInfoMapper extends BaseMapper<PrizeInfoPO> {
}
