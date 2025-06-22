package com.lucky.domain;

import com.lucky.domain.entity.WechatUserEntity;
import com.lucky.domain.exception.BusinessException;
import com.lucky.domain.repository.WechatUserRepository;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Component

public class WechatUserService {
    private final WechatUserRepository wechatUserRepository;


    public WechatUserService(WechatUserRepository wechatUserRepository) {
        this.wechatUserRepository = wechatUserRepository;

    }

    /**
     * 更据手机号码获取用户
     */
    public WechatUserEntity getByPhone(String phone) {
        if (Strings.isBlank(phone))
            return null;
        return wechatUserRepository.getByPhone(phone);
    }

    /**
     * 添加/修改用户
     */
    public Long saveOrUpdate(WechatUserEntity wechatUserEntity) {
        return wechatUserRepository.saveOrUpdate(wechatUserEntity);
    }

    /**
     * 列表
     */
    public List<WechatUserEntity> list(WechatUserEntity wechatUserEntity) {
        return wechatUserRepository.list(wechatUserEntity);
    }

    /**
     * 更据id查询
     */
    public WechatUserEntity getById(Long id) {
        if (Objects.isNull(id))
            return null;
        return wechatUserRepository.getById(id);
    }

    /**
     * 更据openid查询
     */
    public WechatUserEntity getByOpenId(String openId) {
        if (Strings.isBlank(openId))
            return null;
        return wechatUserRepository.getByOpenId(openId);
    }


    public List<WechatUserEntity> getByIds(List<Long> wechatUserIds) {
        return wechatUserRepository.getByIds(wechatUserIds);
    }

    /**
     * 扣减余额
     */
    public  void  balanceReduce(Long wechatUserId, BigDecimal money, String remark) {

        var wechatUserEntity = wechatUserRepository.getById(wechatUserId);

        if (wechatUserEntity.getBalance().compareTo(money) < 0)
            throw BusinessException.newInstance("余额不足");

        wechatUserEntity.setBalance(wechatUserEntity.getBalance().subtract(money));

         wechatUserRepository.saveOrUpdate(wechatUserEntity);
    }
}
