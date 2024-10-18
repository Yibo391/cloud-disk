package com.cloud.service;

import com.cloud.mapper.UserMapper;
import com.cloud.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    // 密码加密器
    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // 根据用户名加载用户信息
    public User loadUserByUsername(String username) {
        return userMapper.loadUserByUsername(username);
    }

    // 注册新用户
    public int registerUser(String username, String password, String role) {
        // 检查用户名是否已存在
        if (userMapper.loadUserByUsername(username) != null) {
            // 返回 -1 表示用户名已存在
            return -1;
        }

        // 创建新用户对象
        User user = new User();
        user.setUsername(username);
        // 加密密码
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(role);

        // 插入用户到数据库
        return userMapper.insertUser(user);
    }
}
