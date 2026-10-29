package com.study.skillup.member.controller;

import com.study.skillup.common.dto.ResponseDto;
import com.study.skillup.member.dto.JoinRequest;
import com.study.skillup.member.service.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


// 회원 API(가입) + 연습용
@RestController
public class MemberRestController {

    private final MemberService memberService;

    public MemberRestController(MemberService memberService) {
        this.memberService = memberService;
    }

    // ResponseEntity<> 연습용
    @GetMapping("/hello")
    public ResponseEntity<String> hello(){
        return ResponseEntity.ok("안녕하세요");
    }

    // 회원가입
    @PostMapping("/api/member/join")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseDto<Void> saveMember(@ModelAttribute JoinRequest req){
        memberService.join(req);

        return new ResponseDto<>(null, null);
    }
}
