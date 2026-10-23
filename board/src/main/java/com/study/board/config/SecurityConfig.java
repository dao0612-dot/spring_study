package com.study.board.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

// 시큐리티 설정: 목록·상세·목록 API는 공개, 나머지는 로그인 필요
@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth -> auth.requestMatchers("/board/list", "/board/detail/**", "/api/board/list", "/error").permitAll().anyRequest().authenticated()).formLogin(Customizer.withDefaults());

        return http.build();
    }
}
