package com.example.demo.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by macro on 2019/9/30.
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @GetMapping("/getUserByAuth")
    public Object getUserByAuth(Authentication authentication) {
        return authentication.getPrincipal();
    }

    @GetMapping("/getUserNoAuth")
    public Object getUserNoAuth(Authentication authentication) {
        return "不需要验证就获取：" + authentication.getPrincipal();
    }
}
