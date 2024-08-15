package com.ecjtu.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ecjtu.dto.pojo.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * Created with Intellij IDEA.
 *
 * @Author: zws
 * @Date: 2024-08-12
 * @Description:
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
    User findUser(User user);
}
