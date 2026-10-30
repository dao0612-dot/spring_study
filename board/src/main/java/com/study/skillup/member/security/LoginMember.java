package com.study.skillup.member.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

// 로그인한 회원 정보: 시큐리티 User(아이디·암호화된 비밀번호·권한)에 회원 번호를 더함
// 로그인 성공 시 세션에 저장되고, 작성자 연결 때 @AuthenticationPrincipal로 꺼내 씀
public class LoginMember extends User {

    private final int memberIdx;

    public LoginMember(int memberIdx, String userId, String pwd,
                       Collection<? extends GrantedAuthority> authorities) {
        super(userId, pwd, authorities);   // 부모(User)에게 아이디·비밀번호·권한을 넘겨 채우게 함
        this.memberIdx = memberIdx;        // 우리가 더한 칸만 직접 채움
    }

    public int getMemberIdx() {
        return memberIdx;
    }
}
