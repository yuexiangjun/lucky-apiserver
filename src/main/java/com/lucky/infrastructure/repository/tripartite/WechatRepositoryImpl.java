package com.lucky.infrastructure.repository.tripartite;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lucky.domain.exception.BusinessException;
import com.lucky.domain.repository.tripartite.WechatRepository;
import com.lucky.domain.valueobject.Code2Session;
import com.lucky.domain.valueobject.WechatConfigValueObject;
import com.lucky.domain.valueobject.WechatPhone;
import com.lucky.infrastructure.repository.config.WechatConfig;
import com.lucky.infrastructure.repository.utils.SendOutUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;


@Component
@Slf4j
public class WechatRepositoryImpl implements WechatRepository {
    private final WechatConfig wechatConfig;
    private final StringRedisTemplate redisTemplate;

    public WechatRepositoryImpl(WechatConfig wechatConfig, StringRedisTemplate redisTemplate) {
        this.wechatConfig = wechatConfig;
        this.redisTemplate = redisTemplate;
    }

    /**
     * 获取配置
     */
    public WechatConfigValueObject getConfig() {
        return new WechatConfigValueObject(wechatConfig.getAppid(), wechatConfig.getSecret());
    }

    @SneakyThrows
    public String getAccessToken() {
        String accessToken = redisTemplate.opsForValue().get("wechat_access_token");
        log.info("获取accessToken{}", accessToken);
        if (Objects.nonNull(accessToken)) {
            return accessToken;
        }
        String url = wechatConfig.getAccessTokenUrl();
        log.info(wechatConfig.getAppid(), "asd");
        log.info("appid{},asecret{}", wechatConfig.getAppid(), wechatConfig.getSecret());
        url = url.replace("APPID", wechatConfig.getAppid()).replace("SECRET", wechatConfig.getSecret());
        String s = SendOutUtils.sendGet(url);
        log.info(s);
        JSONObject jsonObject = JSON.parseObject(s);
        accessToken = jsonObject.getString("access_token");
        redisTemplate.opsForValue().set("wechat_access_token", accessToken, 2, TimeUnit.HOURS);
        log.info("获取accessToken2{}", accessToken);
        return accessToken;
    }

    public WechatPhone getPhoneNumberToken(String code) {
        String accessToken = this.getAccessToken();
        String getPhoneNumberUrl = wechatConfig.getPhoneNumberUrl();
        String getPhoneNumberTokenUrl = getPhoneNumberUrl.replace("ACCESS_TOKEN", accessToken);
        log.info("获取当前手机号url{}", getPhoneNumberTokenUrl);
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("code", code);
        String jsonString = JSON.toJSONString(params);
        String body = SendOutUtils.sendPost(getPhoneNumberTokenUrl, jsonString);
        log.info("获取当前手机号的返回值{}", body);
        JSONObject jsonObject1 = JSON.parseObject(body);
        String errcode = jsonObject1.getString("errcode");
        switch (errcode) {
            case "40029":
                throw BusinessException.newInstance("code无效");
            case "45011":
                throw BusinessException.newInstance("API 调用太频繁，请稍候再试");
            case "40013":
                throw BusinessException.newInstance("请求appid身份与获取code的小程序appid不匹配");

            case "0":
                JSONObject jsonObject = JSON.parseObject(body);
                JSONObject phoneInfo = jsonObject.getJSONObject("phone_info");
                String phoneNumber = phoneInfo.getString("phoneNumber");
                String purePhoneNumber = phoneInfo.getString("purePhoneNumber");
                String countryCode = phoneInfo.getString("countryCode");

                if (Objects.isNull(phoneNumber)) {
                    throw BusinessException.newInstance("当期手机号获取失败");
                }
                return new WechatPhone(phoneNumber, purePhoneNumber, countryCode);
            default:
                redisTemplate.delete("wechat_access_token");
                throw BusinessException.newInstance("系统繁忙，稍候再试");
        }


    }

    /**
     * 小程序登录
     */
    public Code2Session code2Session(String jsCode) {
        var accessToken = this.getAccessToken();
        var code2SessionUrl = wechatConfig.getCode2SessionUrl();
        var appid = wechatConfig.getAppid();
        var secret = wechatConfig.getSecret();

        code2SessionUrl = code2SessionUrl.replace("APPID", wechatConfig.getAppid())
                .replace("SECRET", wechatConfig.getSecret())
                .replace("JSCODE", jsCode)
                .replace("ACCESS_TOKEN", accessToken);
        var body = SendOutUtils.sendGet(code2SessionUrl);
        log.info("小程序登录的返回值{}", body);
        JSONObject jsonObject1 = JSON.parseObject(body);
        String errcode = jsonObject1.getString("errcode");
        if (Objects.isNull(errcode)) {
            var openid = jsonObject1.getString("openid");
            var sessionKey = jsonObject1.getString("session_key");
            if (Strings.isBlank(sessionKey)) {
                throw BusinessException.newInstance("当期小程序登录参数失败");
            }
            return Code2Session.builder()
                    .sessionKey(sessionKey)
                    .openid(openid)
                    .build();
        }
        switch (errcode) {
            case "40029":
                throw BusinessException.newInstance("code无效");
            case "45011":
                throw BusinessException.newInstance("API 调用太频繁，请稍候再试");
            case "40226":
                throw BusinessException.newInstance("高风险等级用户，小程序登录拦截");

            case "0":

                var openid = jsonObject1.getString("openid");
                var sessionKey = jsonObject1.getString("session_key");


                if (Strings.isBlank(sessionKey)) {
                    throw BusinessException.newInstance("当期小程序登录参数失败");
                }
                return Code2Session.builder()
                        .sessionKey(sessionKey)
                        .openid(openid)
                        .build();
            default:
                redisTemplate.delete("wechat_access_token");
                throw BusinessException.newInstance("系统繁忙，稍候再试");
        }
    }

}
