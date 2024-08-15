package com.ecjtu.controller;

import com.ecjtu.dto.result.Result;
import com.ecjtu.server.LogoutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created with Intellij IDEA.
 *
 * @Author: zws
 * @Date: 2024-08-15
 * @Description:
 */
@RestController
@RequestMapping("/logout")
public class LogoutController {
    @Autowired
    private LogoutService logoutService;
    @GetMapping
    public Result logout(){
        return logoutService.logout();
    }

}
