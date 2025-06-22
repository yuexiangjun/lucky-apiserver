package com.lucky.application.tripartite;

import com.lucky.application.WechatUserServer;
import com.lucky.application.interceptor.JwtUtils;
import com.lucky.application.interceptor.TokenEntity;
import com.lucky.domain.entity.WechatUserEntity;
import com.lucky.domain.tripartite.WechatService;
import com.lucky.domain.valueobject.Code2Session;
import com.lucky.domain.valueobject.WechatConfigValueObject;
import com.lucky.domain.valueobject.WechatPhone;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Objects;

@Component
public class WechatServer {
    private final WechatService wechatService;
    private final WechatUserServer wechatUserServer;

    public WechatServer(WechatService wechatService, WechatUserServer wechatUserServer) {
        this.wechatService = wechatService;
        this.wechatUserServer = wechatUserServer;
    }


    /**
     * 获取微信配置
     *
     * @return
     */
    public WechatConfigValueObject getConfig() {
        return wechatService.getConfig();
    }


    /**
     * 获取微信accessToken
     *
     * @return
     */
    public String getAccessToken() {
        return wechatService.getAccessToken();
    }


    /**
     * 根据获取手机号码的组件code获取手机号码
     *
     * @param code
     * @return
     */

    public WechatPhone getPhoneNumberToken(String code) {
        return wechatService.getPhoneNumberToken(code);
    }

    /**
     * 小程序登录参数
     */
    public Code2Session code2Session(String jsCode) {
        var code2Session = wechatService.code2Session(jsCode);
        var wechatUserEntity = wechatUserServer.getByOpenId(code2Session.getOpenid());

        if (Objects.nonNull(wechatUserEntity)) {

            var token = getToken(wechatUserEntity);

            code2Session.setAuthorization(token);
            code2Session.setWechatUserId(wechatUserEntity.getId());


            wechatUserEntity.setLastLoginTime(LocalDateTime.now());
            wechatUserServer.saveOrUpdate(wechatUserEntity);
        }


        return code2Session;
    }

    private static String getToken(WechatUserEntity wechatUserEntity) {
        var tokenEntity = TokenEntity.builder()
                .userId(String.valueOf(wechatUserEntity.getId()))
                .username(wechatUserEntity.getOpenid())
                .client(2)
                .createTime(String.valueOf(System.currentTimeMillis()))
                .build();

        var token = JwtUtils.createToken(tokenEntity);
        return token;
    }



    public Code2Session register2(String jsCode, String phoneCode) {
        //获取手机号码
        var phone = this.getPhoneNumberToken(phoneCode);
        //获取openId
        var code2Session = this.code2Session(jsCode);

        var entity = WechatUserEntity
                .builder()
                .openid(code2Session.getOpenid())
                .phone(phone.getPurePhoneNumber())
                .createTime(LocalDateTime.now())
                .enabled(true)
                .build();
        entity.setLastLoginTime(LocalDateTime.now());
        var id = wechatUserServer.saveOrUpdate(entity);

        entity.setId(id);

        String token = getToken(entity);

        return Code2Session.builder()
                .authorization(token)
                .openid(code2Session.getOpenid())
                .wechatUserId(id)
                .build();


    }
}
