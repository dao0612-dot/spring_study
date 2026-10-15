package com.study.board.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class MemberRestController {

    @GetMapping("/hello")
    public String hello(){
        return "안녕하세요!";
    }

    @GetMapping("/hello2")
    public Map<String, Integer> hello2(){
        Map<String, Integer> hello2 = new HashMap<>();
        hello2.put("안녕하세요?",1);
        hello2.put("반가워요?",2);
        return hello2;
    }
}
