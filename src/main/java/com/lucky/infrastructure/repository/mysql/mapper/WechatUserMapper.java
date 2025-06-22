package com.lucky.infrastructure.repository.mysql.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lucky.infrastructure.repository.mysql.po.WechatUserPO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface WechatUserMapper extends BaseMapper<WechatUserPO> {

}
