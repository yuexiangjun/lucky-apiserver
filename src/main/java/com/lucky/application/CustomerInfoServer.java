package com.lucky.application;

import com.lucky.domain.CustomerInfoService;
import com.lucky.domain.entity.CustomerInfoEntity;
import com.lucky.domain.repository.CustomerInfoRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * 客服信息
 */
@Component
public class CustomerInfoServer {
    private final CustomerInfoService customerInfoService;

    public CustomerInfoServer(CustomerInfoService customerInfoService) {
        this.customerInfoService = customerInfoService;
    }


    /**
     * 添加修改
     */
    @Transactional (rollbackFor = Exception.class)
    public   void  saveOrUpdate(CustomerInfoEntity entity) {
        Long l = customerInfoService.saveOrUpdate(entity);
        if (Objects.isNull(l))
            throw new RuntimeException("添加/修改失败");

    }


    /**
     * 查询所有
     */
    public  List<CustomerInfoEntity> getList() {
        return customerInfoService.getList();
    }




}
