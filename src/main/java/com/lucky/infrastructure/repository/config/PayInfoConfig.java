package com.lucky.infrastructure.repository.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data

@Component
@ConfigurationProperties(prefix = "wx.pay")
public class PayInfoConfig {

    //小程序appid
    private String appid;
    //商户号
    private String machid;
    //证书序列号
    private String mchserialno;

    //小程序秘钥
    private String appsecret;
    //api秘钥
    private String apikey;
    //回调接口地址
    private String notifyurl="/wechat/pay/callback";
    //证书地址
    private String keypath;
    private String notifyhost;



}
