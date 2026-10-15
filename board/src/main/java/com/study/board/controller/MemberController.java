package com.study.board.controller;

import com.study.board.VO.MemberVo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MemberController {

    @GetMapping("/")
    public String index(Model model){
        model.addAttribute("main","메인 화면으로 갑니다.");
        return "index";
    }

    @GetMapping("/hello3")
    @ResponseBody
    public String hello(){
        return "안녕하세요!";
    }
}
