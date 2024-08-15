package com.ecjtu.controller;

import com.ecjtu.dto.result.Result;
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
@RequestMapping("/test")
public class TestController {
    @GetMapping
    public Result test(){
        return Result.success("测试成功",null);
    }
}
