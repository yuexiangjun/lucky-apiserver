package com.lucky.infrastructure.repository.mysql.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lucky.infrastructure.repository.mysql.po.NoCounterPO;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author qiuys
 * @description 针对表【no_counter(公共表 - CRM-编号计数器)】的数据库操作Mapper
 * @createDate 2025-06-06 16:35:38
 * @Entity com.kc.wms.model.mysql.NoCounter
 */
@Mapper
public interface NoCounterMapper extends BaseMapper<NoCounterPO> {

}




