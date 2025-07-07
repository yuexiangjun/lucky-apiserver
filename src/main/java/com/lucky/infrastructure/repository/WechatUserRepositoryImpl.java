package com.lucky.infrastructure.repository;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lucky.domain.entity.WechatUserEntity;
import com.lucky.domain.repository.WechatUserRepository;
import com.lucky.infrastructure.repository.mysql.mapper.WechatUserMapper;
import com.lucky.infrastructure.repository.mysql.po.WechatUserPO;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class WechatUserRepositoryImpl extends ServiceImpl<WechatUserMapper, WechatUserPO> implements WechatUserRepository {

    @Override
    public WechatUserEntity getByPhone(String phone) {

        var wrapper = Wrappers.lambdaQuery(WechatUserPO.class)
                .eq(WechatUserPO::getPhone, phone);

        var one = this.getOne(wrapper);

        return WechatUserPO.toEntity(one);

    }

    @Override
    public Long saveOrUpdate(WechatUserEntity entity) {

        var po = WechatUserPO.getInstance(entity);

        if (Objects.isNull(po))
            return null;

        this.saveOrUpdate(po);

        return po.getId();
    }

    @Override
    public List<WechatUserEntity> list(WechatUserEntity entity) {

        var wrapper = Wrappers.lambdaQuery(WechatUserPO.class)
                .like(Strings.isNotBlank(entity.getPhone()), WechatUserPO::getPhone, entity.getPhone())
                .eq(Strings.isNotBlank(entity.getOpenid()), WechatUserPO::getOpenid, entity.getOpenid())
                .like(Strings.isNotBlank(entity.getName()), WechatUserPO::getName, entity.getName())
                .eq(Objects.nonNull(entity.getOwnerId()), WechatUserPO::getOwnerId, entity.getOwnerId())
                .orderByDesc(WechatUserPO::getCreateTime);

        return this.list(wrapper)
                .stream()
                .map(WechatUserPO::toEntity)
                .collect(Collectors.toList());

    }

    @Override
    public WechatUserEntity getById(Long id) {
        var wrapper = Wrappers.lambdaQuery(WechatUserPO.class)
                .eq(WechatUserPO::getId, id);
        var one = this.getOne(wrapper);

        return WechatUserPO.toEntity(one);
    }

    @Override
    public WechatUserEntity getByOpenId(String openId) {
        var wrapper = Wrappers.lambdaQuery(WechatUserPO.class)
                .eq(WechatUserPO::getOpenid, openId);

        var one = this.getOne(wrapper);

        return WechatUserPO.toEntity(one);
    }

    @Override
    public List<WechatUserEntity> getByIds(List<Long> wechatUserIds) {
        if (CollectionUtils.isEmpty(wechatUserIds))
            return List.of();
        var wrapper = Wrappers.lambdaQuery(WechatUserPO.class)
                .in(WechatUserPO::getId, wechatUserIds);
        return this.list(wrapper)
                .stream()
                .map(WechatUserPO::toEntity)
                .collect(Collectors.toList());
    }

    @Override
    public WechatUserEntity getWechatUserEntity(WechatUserEntity wechatUserEntity) {
        var wrapper = Wrappers.lambdaQuery(WechatUserPO.class)
                .eq(WechatUserPO::getOpenid, wechatUserEntity.getOpenid())
                .eq(WechatUserPO::getPhone, wechatUserEntity.getPhone())
                .eq(WechatUserPO::getName, wechatUserEntity.getName());
        var one = this.getOne(wrapper, false);
        return WechatUserPO.toEntity(one);
    }


    @Override
    public List<WechatUserEntity> listByTime(WechatUserEntity entity, LocalDateTime startTime, LocalDateTime endTime) {
        var wrapper = Wrappers.lambdaQuery(WechatUserPO.class)
                .eq(Strings.isNotBlank(entity.getPhone()), WechatUserPO::getPhone, entity.getPhone())
                .eq(Strings.isNotBlank(entity.getOpenid()), WechatUserPO::getOpenid, entity.getOpenid())
                .eq(Strings.isNotBlank(entity.getName()), WechatUserPO::getName, entity.getName())

                .ge(startTime != null, WechatUserPO::getCreateTime, startTime)
                .le(endTime != null, WechatUserPO::getCreateTime, endTime);
        return this.list(wrapper)
                .stream()
                .map(WechatUserPO::toEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<WechatUserEntity> getUserNameOrPhone(String userNameOrPhone) {


        var wrapper = Wrappers.lambdaQuery(WechatUserPO.class)
                .and(wrapper1 -> wrapper1.like(Strings.isNotBlank(userNameOrPhone), WechatUserPO::getName, userNameOrPhone).or()
                        .like(Strings.isNotBlank(userNameOrPhone), WechatUserPO::getPhone, userNameOrPhone));

        return this.list(wrapper)
                .stream()
                .map(WechatUserPO::toEntity)
                .collect(Collectors.toList());
    }
}
