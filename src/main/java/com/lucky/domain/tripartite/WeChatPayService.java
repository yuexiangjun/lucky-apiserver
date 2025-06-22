package com.lucky.domain.tripartite;

import com.alibaba.fastjson.JSONObject;
import com.lucky.domain.repository.tripartite.WeChatPayRepository;
import com.lucky.domain.valueobject.PayInfo;
import com.lucky.domain.valueobject.PayOrderPram;
import com.lucky.domain.valueobject.PayOrderReturnValue;
import org.springframework.stereotype.Component;

@Component
public class WeChatPayService {
    private final WeChatPayRepository weChatPayRepository;

    public WeChatPayService(WeChatPayRepository weChatPayRepository) {
        this.weChatPayRepository = weChatPayRepository;
    }

    /**
     * 支付
     */
    public PayInfo pay(PayOrderPram payOrderPram) {
        return weChatPayRepository.pay(payOrderPram);
    }

    ;

    /**
     * 回调
     */
    public PayOrderReturnValue payCallBack(JSONObject jsonObject) {
        return weChatPayRepository.payCallBack(jsonObject);
    }

    ;

    /**
     * 查询支付结果
     */
    public PayOrderReturnValue payQueryOrder(Long payOrderId) {
        return weChatPayRepository.payQueryOrder(payOrderId);
    }

    ;

}
