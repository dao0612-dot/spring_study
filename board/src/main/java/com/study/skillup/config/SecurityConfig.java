package com.study.skillup.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


// 시큐리티 설정: 주소별 로그인 필요 여부, 로그인 방식, 비밀번호 암호화 도구
@Configuration
public class SecurityConfig {

    // 공개 주소 외에는 로그인 필요, 로그인은 우리 화면(/member/login), 로그아웃은 POST
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/", "/board/list", "/board/detail/**", "/board/fetch",
                                "/api/board/list", "/board/file/download/**",
                                "/member/join", "/api/member/join", "/member/login",
                                "/error"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/member/login")
                        .usernameParameter("userId")
                        .passwordParameter("pwd")
                        .defaultSuccessUrl("/board/list")
                )
                .logout(logout -> logout
                        .logoutUrl("/member/logout")
                        .logoutSuccessUrl("/board/list")
                );

        return http.build();
    }

    // 비밀번호 암호화·비교 도구(BCrypt): 가입 때 encode, 로그인 때 시큐리티가 matches
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
