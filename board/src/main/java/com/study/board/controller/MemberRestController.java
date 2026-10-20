package com.study.board.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class MemberRestController {

    // ResponseEntity<> 연습용
    @GetMapping("/hello")
    public ResponseEntity<String> hello(){
        return ResponseEntity.ok("안녕하세요");
    }

    // Map<> 연습용
    @GetMapping("/hello2")
    public Map<Integer, String> hello2(){
        Map<Integer, String> hello2 = new HashMap<>();
        hello2.put(1, "안녕하세요?");
        hello2.put(2, "반가워요?");
        return hello2;
    }
}
