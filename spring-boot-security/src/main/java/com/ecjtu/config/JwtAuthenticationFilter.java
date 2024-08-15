package com.ecjtu.config;

import com.ecjtu.dto.pojo.UserLoginDetails;
import com.ecjtu.utils.JWTProperties;
import com.ecjtu.utils.JWTUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

/**
 * Created with Intellij IDEA.
 *
 * @Author: zws
 * @Date: 2024-08-15
 * @Description: 自定义过滤器，该过滤器只会被调用一次，用于对用户身份进行验证
 */
@Component
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    private RedisCacheUtilConfig redisCacheUtilConfig;
    @Autowired
    private JWTProperties jwtPro;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String tokenName = jwtPro.getTokenName();
        String token = request.getHeader(tokenName);
        if(!StringUtils.hasText(token)){
            // 请求头中不含token字段，即一些静态资源的请求
            // 放行
            filterChain.doFilter(request,response);
            return;
        }
        Claims claims = null;
        try {
            claims = JWTUtil.parseToken(token,jwtPro.getSecretKey());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("token解析异常");
        }
        String userId = claims.getSubject();
        String redisKey = "login:"+userId;
        // redis中获取用户信息
        UserLoginDetails loginUser = (UserLoginDetails) redisCacheUtilConfig.getObjectForCache(redisKey);
        if (Objects.isNull(loginUser)){
            throw new NullPointerException("用户未登录");
        }
        // TODO 获取权限信息放入authenticationToken中
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginUser, null, null);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        log.info("token => {}",token);
        log.info("userId =>{}",userId);
        log.info("UserLoginDetails => {}",loginUser.getUser());
        filterChain.doFilter(request,response);
    }
}
