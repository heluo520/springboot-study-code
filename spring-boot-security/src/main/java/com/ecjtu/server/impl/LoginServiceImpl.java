package com.ecjtu.server.impl;

import com.ecjtu.config.RedisCacheUtilConfig;
import com.ecjtu.dto.pojo.User;
import com.ecjtu.dto.pojo.UserLoginDetails;
import com.ecjtu.dto.result.Result;
import com.ecjtu.server.LoginService;
import com.ecjtu.utils.JWTProperties;
import com.ecjtu.utils.JWTUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * Created with Intellij IDEA.
 *
 * @Author: zws
 * @Date: 2024-08-15
 * @Description: 验证逻辑
 */
@Service
@Slf4j
public class LoginServiceImpl implements LoginService {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private RedisCacheUtilConfig redisCache;
    @Autowired
    private JWTProperties jwtPro;
    @Override
    public Result login(User user) {
        //创建一个包含用户登录提交的用户名密码的认证请求
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
        //使用认证方法进行认证，认证逻辑在实现了UserDetailsService接口的类中的loadUserByUsername方法里，
        //该方法返回包含用户信息的一个封装对象UserDetails，该对象需要我们自己定义，即实现UserDetails接口
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        if (Objects.isNull(authenticate)){
            throw new NullPointerException("用户不存在");
        }
        //获取认证主体信息，即UserDetails对象
        UserLoginDetails userLoginDetails = (UserLoginDetails) authenticate.getPrincipal();
        log.info("UserLoginDetails => {}",userLoginDetails);
        String id = userLoginDetails.getUser().getId().toString();
        //创建token
        String token = JWTUtil.createToken(id,jwtPro.getTtl(),jwtPro.getSecretKey(),jwtPro.getIssuer(),new HashMap<>());
        //将认证信息放入redis
        redisCache.setObjectCache("login:"+id,userLoginDetails);
        Map<String,String> map = new HashMap<>();
        map.put("token",token);
        //返回封装好的结果
        return Result.success("登录成功",map);
    }
}
