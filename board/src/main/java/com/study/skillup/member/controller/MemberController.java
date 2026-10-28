package com.study.skillup.member.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

// 연습용
@Controller
public class MemberController {

    // 메인 화면
    @GetMapping("/")
    public String index(Model model){
        model.addAttribute("main","메인 화면으로 갑니다.");
        return "index";
    }

    // @ResponseBody 연습용
    @GetMapping("/hello3")
    @ResponseBody
    public String hello(){
        return "안녕하세요!";
    }
}
