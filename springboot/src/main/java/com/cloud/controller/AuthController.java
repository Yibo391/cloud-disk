package com.cloud.controller;

import com.cloud.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public String register(@RequestParam String username,
                           @RequestParam String password,
                           @RequestParam(required = false, defaultValue = "USER") String role) {
        System.out.println("收到注册请求：username=" + username);
        int result = userService.registerUser(username, password, role);
        if (result > 0) {
            System.out.println("注册成功");
            return "注册成功";
        } else if (result == -1) {
            System.out.println("用户名已存在");
            return "用户名已存在";
        } else {
            System.out.println("注册失败");
            return "注册失败";
        }
    }
}
