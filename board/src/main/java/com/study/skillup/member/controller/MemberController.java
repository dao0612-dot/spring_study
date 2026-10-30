package com.study.skillup.member.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

// 회원 화면(가입) + 메인·연습용
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


    // @ResponseBody 연습용
    @GetMapping("/hello3")
    @ResponseBody
    public String hello(){
        return "안녕하세요!";
    }
}
