package com.study.skillup.member.dto;


// 가입 요청 데이터: 가입 화면이 보낸 아이디·비밀번호(원문)·이름
public record JoinRequest(String userId, String pwd, String name) {

    // 로그에 비밀번호 원문이 남지 않도록 pwd는 빼고 출력
    @Override
    public String toString(){
        return "JoinRequest[userId="+userId+", name="+name+"]";
    }
}
