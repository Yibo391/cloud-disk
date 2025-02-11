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

    // Password encoder
    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // Load user information by username
    public User loadUserByUsername(String username) {
        return userMapper.loadUserByUsername(username);
    }

    // Register new user
    public int registerUser(String username, String password, String role) {
        // Check if username already exists
        if (userMapper.loadUserByUsername(username) != null) {
            // Return -1 if username already exists
            return -1;
        }

        // Create new user object
        User user = new User();
        user.setUsername(username);
        // Encrypt password
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(role);

        // Insert user into database
        return userMapper.insertUser(user);
    }
}
