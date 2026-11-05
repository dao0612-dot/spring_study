package com.study.skillup.member.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

// 회원 화면(가입·로그인) + 메인
@Controller
public class MemberController {

    // 메인 화면
    @GetMapping("/")
    public String index(){
        return "index";
    }

    // 회원가입 화면
    @GetMapping("/member/join")
    public String memberJoin(){
        return "member/join";
    }

    // 로그인 화면(처리는 시큐리티 필터)
    @GetMapping("/member/login")
    public String memberLogin(){
        return "member/login";
    }
}
