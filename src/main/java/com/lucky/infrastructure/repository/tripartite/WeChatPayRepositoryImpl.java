package com.lucky.infrastructure.repository.tripartite;

import com.alibaba.fastjson.JSONObject;
import com.lucky.domain.repository.tripartite.WeChatPayRepository;
import com.lucky.domain.valueobject.PayInfo;
import com.lucky.domain.valueobject.PayOrderPram;
import com.lucky.domain.valueobject.PayOrderReturnValue;
import com.lucky.infrastructure.repository.config.PayInfoConfig;
import com.lucky.infrastructure.repository.utils.AesUtil;
import com.wechat.pay.java.core.Config;
import com.wechat.pay.java.core.RSAAutoCertificateConfig;
import com.wechat.pay.java.service.payments.jsapi.JsapiServiceExtension;
import com.wechat.pay.java.service.payments.jsapi.model.Amount;
import com.wechat.pay.java.service.payments.jsapi.model.Payer;
import com.wechat.pay.java.service.payments.jsapi.model.PrepayRequest;
import com.wechat.pay.java.service.payments.jsapi.model.QueryOrderByOutTradeNoRequest;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;

@Component
@Slf4j
public class WeChatPayRepositoryImpl implements WeChatPayRepository {
    private final PayInfoConfig payInfoConfig;

    public WeChatPayRepositoryImpl(PayInfoConfig payInfoConfig) {
        this.payInfoConfig = payInfoConfig;
    }

    @Override
    public PayInfo pay(PayOrderPram payOrderPram) {
        var service = this.getJsapiServiceExtension();

        var request = new PrepayRequest();
        var amount = new Amount();

        var total = payOrderPram.getPayMoney()
                .multiply(BigDecimal.valueOf(100))
                .setScale(0, BigDecimal.ROUND_HALF_UP)
                .toBigInteger().intValue();

        amount.setTotal(total);
        request.setAmount(amount);
        request.setAppid(payInfoConfig.getAppid());
        Payer payer = new Payer();
        payer.setOpenid(payOrderPram.getOpenId());
        request.setPayer(payer);
        request.setMchid(payInfoConfig.getMachid());
        request.setDescription(payOrderPram.getPayDesc());
        request.setNotifyUrl(payInfoConfig.getNotifyhost().concat(payInfoConfig.getNotifyurl()));
        request.setOutTradeNo(String.valueOf(payOrderPram.getPayOrderId()));
        // 调用下单方法，得到应答
        var prepayWithRequestPaymentResponse = service.prepayWithRequestPayment(request);

        var response = JSONObject.parseObject(JSONObject.toJSONString(prepayWithRequestPaymentResponse));

        return PayInfo.builder()
                .payParams(response)
                .payOrderId(payOrderPram.getPayOrderId())
                .build();

    }

    private JsapiServiceExtension getJsapiServiceExtension() {
        //构建微信支付参数
        // 使用微信支付公钥的RSA配置
        Config config = new RSAAutoCertificateConfig.Builder()
                .merchantId(payInfoConfig.getMachid())
                .privateKeyFromPath(payInfoConfig.getKeypath())
                .merchantSerialNumber(payInfoConfig.getMchserialno())
                .apiV3Key(payInfoConfig.getApikey())
                .build();

        // 构建service
        return new JsapiServiceExtension.Builder().config(config).build();

    }

    /**
     * 支付订单查询
     */
    public PayOrderReturnValue payQueryOrder(Long payOrderId) {
        var service = this.getJsapiServiceExtension();
        var request = new QueryOrderByOutTradeNoRequest();
        request.setOutTradeNo(String.valueOf(payOrderId));
        request.setMchid(payInfoConfig.getMachid());
        // 调用接口
        var transaction = service.queryOrderByOutTradeNo(request);

        return PayOrderReturnValue.builder()
                .payOrderId(payOrderId)
                .payResult(transaction.getTradeState().name())
                .thirdPayId(transaction.getTransactionId())
                .build();

    }

    /**
     * 支付回调
     */
    @SneakyThrows
    public PayOrderReturnValue payCallBack(JSONObject jsonObject) {

        var resource = jsonObject.getJSONObject("resource");
        var associatedData = resource.getString("associated_data");
        var nonce = resource.getString("nonce");
        var ciphertext = resource.getString("ciphertext");
        var text = new AesUtil(payInfoConfig.getApikey().getBytes(StandardCharsets.UTF_8))
                .decryptToString(
                        associatedData.getBytes(StandardCharsets.UTF_8),
                        nonce.getBytes(StandardCharsets.UTF_8),
                        ciphertext);

        var parse = JSONObject.parseObject(text);
        var outTradeNo = parse.getLong("out_trade_no");
        var transactionId = parse.getString("transaction_id");
        var tradeState = parse.getString("trade_state");

        return PayOrderReturnValue.builder()
                .payOrderId(outTradeNo)
                .payResult(tradeState)
                .thirdPayId(transactionId)
                .build();

    }


}
