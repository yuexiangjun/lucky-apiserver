package com.lucky.infrastructure.repository.mysql.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lucky.infrastructure.repository.mysql.po.CustomerInfoPO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 客服信息
 */

@Mapper
public interface CustomerInfoMapper extends BaseMapper<CustomerInfoPO> {


}
