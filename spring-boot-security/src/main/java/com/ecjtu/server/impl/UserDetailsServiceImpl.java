package com.ecjtu.server.impl;

import com.ecjtu.dto.pojo.User;
import com.ecjtu.dto.pojo.UserLoginDetails;
import com.ecjtu.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * Created with Intellij IDEA.
 *
 * @Author: zws
 * @Date: 2024-08-15
 * @Description: 登录验证
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserMapper userMapper;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userMapper.findUser(User.builder().username(username).build());
        if(Objects.isNull(user)){
            throw new UsernameNotFoundException("用户名不存在");
        }
        //TODO 根据用户查询权限信息封装到UserLoginDetails中
        return new UserLoginDetails(user);
    }
}
