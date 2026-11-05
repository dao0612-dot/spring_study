package com.study.skillup.member.service;

import com.study.skillup.member.mapper.MemberMapper;
import com.study.skillup.member.security.LoginMember;
import com.study.skillup.member.vo.MemberVo;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

// 로그인 때 시큐리티가 부르는 부품: 아이디로 회원을 찾아 LoginMember로 돌려줌
// 비밀번호 비교는 여기서 하지 않음(시큐리티가 PasswordEncoder.matches로)
@Service
public class MemberDetailsService implements UserDetailsService {

    private final MemberMapper memberMapper;

    public MemberDetailsService(MemberMapper memberMapper) {
        this.memberMapper = memberMapper;
    }

    // 회원이 없거나 사용 중지(use_yn='N')면 UsernameNotFoundException → 시큐리티가 로그인 실패로 처리
    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        MemberVo member = memberMapper.selectMemberByUserId(userId);

        if (member == null || "N".equals(member.getUseYn())) {
            throw new UsernameNotFoundException("회원 없음 또는 사용 중지");
        }

        return new LoginMember(
                member.getMemberIdx(),
                member.getUserId(),
                member.getPwd(),
                List.of(new SimpleGrantedAuthority("ROLE_" + member.getRole().toUpperCase()))
        );
    }
}
