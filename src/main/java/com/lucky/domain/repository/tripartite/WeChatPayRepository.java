package com.lucky.domain.repository.tripartite;

import com.alibaba.fastjson.JSONObject;
import com.lucky.domain.valueobject.PayInfo;
import com.lucky.domain.valueobject.PayOrderPram;
import com.lucky.domain.valueobject.PayOrderReturnValue;

/**
 * 小程序支付
 */
public interface WeChatPayRepository {
    /**
     * 支付
     */
    PayInfo pay(PayOrderPram payOrderPram);

    /**
     * 回调
     */
    PayOrderReturnValue payCallBack(JSONObject jsonObject);

    /**
     * 查询支付结果
     */
    PayOrderReturnValue payQueryOrder(Long payOrderId);

}
