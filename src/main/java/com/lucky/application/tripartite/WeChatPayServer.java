package com.lucky.application.tripartite;

import com.alibaba.fastjson.JSONObject;
import com.lucky.domain.tripartite.WeChatPayService;
import com.lucky.domain.valueobject.PayInfo;
import com.lucky.domain.valueobject.PayOrderPram;
import com.lucky.domain.valueobject.PayOrderReturnValue;
import org.springframework.stereotype.Component;

@Component
public class WeChatPayServer {

    private final WeChatPayService weChatPayService;

    public WeChatPayServer(WeChatPayService weChatPayService) {
        this.weChatPayService = weChatPayService;
    }

    public PayInfo pay(PayOrderPram payOrderPram) {
        return weChatPayService.pay(payOrderPram);
    }

    public PayOrderReturnValue payQueryOrder( Long payOrderId) {
        return weChatPayService.payQueryOrder(payOrderId);
    }

    public PayOrderReturnValue payCallBack(JSONObject jsonObject) {
        return weChatPayService.payCallBack(jsonObject);
    }

}
