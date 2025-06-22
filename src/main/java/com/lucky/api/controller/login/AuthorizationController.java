package com.lucky.api.controller.login;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 登录
 * @folder API/后台/登录
 */
@RestController
@RequestMapping
public class AuthorizationController {
    @PostMapping("/login")
    public Boolean login(){
        System.out.println("====");
        return true;
    }


}
