package com.ecjtu.controller;

import com.ecjtu.dto.pojo.User;
import com.ecjtu.dto.result.Result;
import com.ecjtu.server.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created with Intellij IDEA.
 *
 * @Author: zws
 * @Date: 2024-08-15
 * @Description:
 */
@RestController
@RequestMapping("/login")
public class LoginController {
    @Autowired
    private LoginService loginService;

    @GetMapping
    public Result login(@RequestParam("username") String username,@RequestParam("password") String password){
        return loginService.login(
                User.builder()
                .username(username)
                .password(password)
                .build());
    }
}
