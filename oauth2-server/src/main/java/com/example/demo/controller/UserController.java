package com.example.demo.controller;

import com.example.demo.domain.User;
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
        User user = (User) authentication.getPrincipal();
        return "验证通过，获取用户：" + user.getUsername();
    }

}
