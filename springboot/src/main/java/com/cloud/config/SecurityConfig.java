package com.cloud.config;

import com.cloud.service.UserDetailsServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
// 导入正确的类
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
// 导入处理器
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
// 更改为 jakarta.servlet 包
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
// 导入必要的异常和工具类
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    // 配置密码编码器
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 配置安全过滤器链
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // 禁用 CSRF（仅用于测试，生产环境应启用）
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/login", "/register", "/error").permitAll() // 允许匿名访问
                        .anyRequest().authenticated() // 其他请求需要认证
                )
                .formLogin(form -> form
                        .loginProcessingUrl("/login") // 登录 URL
                        .successHandler(authenticationSuccessHandler()) // 自定义成功处理器
                        .failureHandler(authenticationFailureHandler()) // 自定义失败处理器
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessHandler(logoutSuccessHandler()) // 自定义注销处理器
                        .permitAll()
                )
                .userDetailsService(userDetailsService);

        return http.build();
    }

    // 自定义认证成功处理器
    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return (request, response, authentication) -> {
            response.setContentType("application/json;charset=UTF-8");
            PrintWriter out = response.getWriter();
            Map<String, Object> result = new HashMap<>();
            result.put("status", 200);
            result.put("message", "登录成功");
            // 可选地，包含用户信息
            result.put("user", authentication.getName());
            out.write(new ObjectMapper().writeValueAsString(result));
            out.flush();
            out.close();
        };
    }

    // 自定义认证失败处理器
    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        return (request, response, exception) -> {
            response.setContentType("application/json;charset=UTF-8");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401
            PrintWriter out = response.getWriter();
            Map<String, Object> result = new HashMap<>();
            result.put("status", 401);
            result.put("message", "登录失败，用户名或密码错误");
            out.write(new ObjectMapper().writeValueAsString(result));
            out.flush();
            out.close();
        };
    }

    // 自定义注销成功处理器
    @Bean
    public LogoutSuccessHandler logoutSuccessHandler() {
        return (request, response, authentication) -> {
            response.setContentType("application/json;charset=UTF-8");
            PrintWriter out = response.getWriter();
            Map<String, Object> result = new HashMap<>();
            result.put("status", 200);
            result.put("message", "退出登录成功");
            out.write(new ObjectMapper().writeValueAsString(result));
            out.flush();
            out.close();
        };
    }
}
