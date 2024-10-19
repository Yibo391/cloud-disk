package com.cloud.config;

import com.cloud.service.UserDetailsServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
// 导入必要的异常和工具类
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
// 导入 CORS 所需的类
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.cors.CorsConfigurationSource;

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
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // 启用 CORS
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/login", "/register", "/error").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginProcessingUrl("/login")
                        .successHandler(authenticationSuccessHandler())
                        .failureHandler(authenticationFailureHandler())
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessHandler(logoutSuccessHandler())
                        .permitAll()
                )
                .userDetailsService(userDetailsService);

        return http.build();
    }

    // 定义 CORS 配置源
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("http://localhost:5173"); // 允许前端的域名
        configuration.addAllowedMethod("*"); // 允许所有 HTTP 方法
        configuration.addAllowedHeader("*"); // 允许所有请求头
        configuration.setAllowCredentials(true); // 允许发送凭证（如 Cookie）
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    // 自定义认证成功处理器
    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return (HttpServletRequest request, HttpServletResponse response, org.springframework.security.core.Authentication authentication) -> {
            System.out.println("AuthenticationSuccessHandler 被调用");
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
        return (HttpServletRequest request, HttpServletResponse response, org.springframework.security.core.AuthenticationException exception) -> {
            System.out.println("AuthenticationFailureHandler 被调用");
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
        return (HttpServletRequest request, HttpServletResponse response, org.springframework.security.core.Authentication authentication) -> {
            System.out.println("LogoutSuccessHandler 被调用");
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
