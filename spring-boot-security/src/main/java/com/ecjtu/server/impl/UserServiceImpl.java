package com.ecjtu.server.impl;

import com.ecjtu.constant.UserEnum;
import com.ecjtu.dto.pojo.User;
import com.ecjtu.dto.result.Result;
import com.ecjtu.mapper.UserMapper;
import com.ecjtu.server.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * Created with Intellij IDEA.
 *
 * @Author: zws
 * @Date: 2024-08-12
 * @Description:
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
    public Result findUser(User user) {
        User u = userMapper.findUser(user);
        Result result = null;
        if(Objects.isNull(u)){
            result = Result.fail(UserEnum.USER_NOT_FOUND.code(),UserEnum.USER_NOT_FOUND.msg(),null);
        }else {
            result = Result.success("ok",u);
        }
        return result;
    }
}
