package com.cloud.mapper;

import com.cloud.model.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    // 根据用户名查询用户信息
    User loadUserByUsername(String username);

    // 插入新用户
    int insertUser(User user);
}
