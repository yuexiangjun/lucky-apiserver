package com.lucky.api.controller.common;

import com.lucky.application.interceptor.JwtUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping(produces = "application/json;charset=UTF-8")
public class BaseController {
    /**
     * 请求体
     */
    @Resource
    protected HttpServletRequest request;

    protected String getToken() {
        return request.getHeader("authorization");
    }

    /**
     * 获取用户的id
     */
    protected Long getAdminUserId() {
        String token = getToken();
        if (token == null)
            return null;
        if (JwtUtils.getClient(token) == 1) {
            return Long.valueOf(JwtUtils.getUserId(token));
        }
        return null;
    }

    /**
     * 获取用户的id
     */
    protected Long getWechatUserId() {
        String token = getToken();
        if (token == null)
            return null;
        if (JwtUtils.getClient(token) == 2) {
            return Long.valueOf(JwtUtils.getUserId(token));
        }
        return null;
    }

}
