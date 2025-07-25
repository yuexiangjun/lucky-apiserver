package com.lucky.application.interceptor;
 

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class LoginConfig implements WebMvcConfigurer {
 
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //注册TestInterceptor拦截器
        InterceptorRegistration registration = registry.addInterceptor(new UserLoginInterceptor());
        registration.addPathPatterns("/**"); //所有路径都被拦截
        registration.excludePathPatterns(    //添加不拦截路径
                "/**/*/login-enroll",                    //登录路径
                "/**/*.html",                //html静态资源
                "/**/*.js",                  //js静态资源
                "/**/*.css"  ,                //css静态资源
                "/api/wechat/tripartite/**",
                "/**/login",
                "/api/wechat/pay/callback",
                "/api/wechat/banner/list" ,
                "/api/wechat/series-topic/*",
                "/api/wechat/pay/buy/queue/num",
                "/api/wechat/detail/*"



                );
    }
}