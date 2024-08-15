package com.ecjtu.server;

import com.ecjtu.dto.pojo.User;
import com.ecjtu.dto.result.Result;

/**
 * Created with Intellij IDEA.
 *
 * @Author: zws
 * @Date: 2024-08-13
 * @Description:
 */
public interface UserService {
    Result findUser(User user);
}
