package com.lucky.infrastructure.repository.mysql.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lucky.infrastructure.repository.mysql.po.SessionInfoPO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SessionInfoMapper extends BaseMapper<SessionInfoPO> {
}
