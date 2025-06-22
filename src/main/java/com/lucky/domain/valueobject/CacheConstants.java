package com.lucky.domain.valueobject;

/**
 * 缓存的key 常量
 * 
 * @author happy
 */
public class CacheConstants
{
    /**
     * 缓存有效期，默认7天
     */
    public final static long EXPIRATION = 60*16;

    /**
     * 缓存刷新时间，默认120（分钟）
     */
    public final static long REFRESH_TIME = 120;

    /**
     * 权限缓存前缀
     */
    public final static String LOGIN_TOKEN_KEY = "login_tokens:";

    /**
     * 微信小程序常量
     */
    public final static String USER_CURRENT_STATUS = "USER_CURRENT_STATUS:";
}
