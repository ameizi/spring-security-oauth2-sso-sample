package com.example.sso.resourceserver.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 示例模块 Controller
 */
@RestController
@RequestMapping("/")
public class UserInfoController {

    @RequestMapping("/userinfo")
    public Authentication authentication(Authentication authentication) {
        return authentication;
    }

}
