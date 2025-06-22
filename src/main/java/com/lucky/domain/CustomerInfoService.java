package com.lucky.domain;

import com.lucky.domain.entity.CustomerInfoEntity;
import com.lucky.domain.repository.CustomerInfoRepository;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 客服信息
 */
@Component
public class CustomerInfoService {
    private final CustomerInfoRepository customerInfoRepository;

    public CustomerInfoService(CustomerInfoRepository customerInfoRepository) {
        this.customerInfoRepository = customerInfoRepository;
    }

    /**
     * 添加修改
     */
    public  Long saveOrUpdate(CustomerInfoEntity entity) {
        return customerInfoRepository.saveOrUpdate(entity);
    }


    /**
     * 查询所有
     */
    public  List<CustomerInfoEntity> getList() {
        return customerInfoRepository.getList();
    }




}
