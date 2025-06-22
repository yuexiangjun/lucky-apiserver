package com.lucky.application;


import com.lucky.application.interceptor.JwtUtils;
import com.lucky.application.interceptor.LoginUserEntity;
import com.lucky.application.interceptor.TokenEntity;
import com.lucky.domain.WechatUserService;
import com.lucky.domain.entity.WechatUserEntity;
import com.lucky.domain.exception.BusinessException;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Objects;

@Component
public class WechatUserServer {
    private final WechatUserService wechatUserService;

    public WechatUserServer(WechatUserService wechatUserService) {
        this.wechatUserService = wechatUserService;
    }


    /**
     * 登录
     */
    public LoginUserEntity login(WechatUserEntity entity) {
        if (entity.getOpenid() == null) {
            throw BusinessException.newInstance("手机号或openid能为空");
        }
        var oldEntity = wechatUserService.getByOpenId(entity.getOpenid());

        if (Objects.isNull(oldEntity)) {
            entity.setEnabled(true);
            entity.setCreateTime(LocalDateTime.now());
            oldEntity = entity;
        } else {
            if (Objects.equals(entity.getEnabled(), 0)) {
                throw BusinessException.newInstance("账号已被禁用");
            }
        }

        oldEntity.setLastLoginTime(LocalDateTime.now());

        var id = wechatUserService.saveOrUpdate(oldEntity);

        var tokenEntity = TokenEntity.builder()
                .userId(String.valueOf(id))
                .username(oldEntity.getOpenid())
                .client(2)
                .createTime(String.valueOf(System.currentTimeMillis()))
                .build();

        var token = JwtUtils.createToken(tokenEntity);

        return LoginUserEntity.builder()
                .authorization(token)
                .username(oldEntity.getName())
                .phone(oldEntity.getPhone())
                .id(oldEntity.getId())
                .build();
    }

    /**
     * 更据openid查询
     */
    public WechatUserEntity getByOpenId(String openId) {
        if (Strings.isBlank(openId))
            return null;
        return wechatUserService.getByOpenId(openId);
    }

    /**
     * 添加/修改用户
     */
    public Long saveOrUpdate(WechatUserEntity wechatUserEntity) {
        return wechatUserService.saveOrUpdate(wechatUserEntity);
    }

    public WechatUserEntity getInfo(Long wechatUserId) {
        return wechatUserService.getById(wechatUserId);
    }
}
