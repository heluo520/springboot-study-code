package com.ecjtu.server.impl;

import com.ecjtu.config.RedisCacheUtilConfig;
import com.ecjtu.dto.pojo.UserLoginDetails;
import com.ecjtu.dto.result.Result;
import com.ecjtu.server.LogoutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 * Created with Intellij IDEA.
 *
 * @Author: zws
 * @Date: 2024-08-15
 * @Description:
 */
@Service
public class LogoutServiceImpl implements LogoutService {
    @Autowired
    private RedisCacheUtilConfig redisCache;
    @Override
    public Result logout() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserLoginDetails loginUser = (UserLoginDetails) authentication.getPrincipal();
        Long userId = loginUser.getUser().getId();
        String key = "login:"+userId.toString();
        boolean b = redisCache.removeObjectForCache(key);
        return Result.success("登出成功",b);
    }
}
