package com.study.skillup.member.service;


import com.study.skillup.common.exception.BlankValueException;
import com.study.skillup.common.exception.DBNotAffectedException;
import com.study.skillup.common.exception.ErrorCode;
import com.study.skillup.common.exception.DuplicateException;
import com.study.skillup.common.exception.InvalidValueException;
import com.study.skillup.member.dto.JoinRequest;
import com.study.skillup.member.mapper.MemberMapper;
import com.study.skillup.member.vo.MemberVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class MemberService {

    //비밀번호 규칙: 8자 이상, 영문/숫자 각각 하나 이상
    private static final String PWD_RULE = "^(?=.*[A-Za-z])(?=.*\\d).{8,}$";

    private final MemberMapper memberMapper;
    private final PasswordEncoder passwordEncoder;

    public MemberService(MemberMapper memberMapper, PasswordEncoder passwordEncoder) {
        this.memberMapper = memberMapper;
        this.passwordEncoder = passwordEncoder;
    }

    // 회원가입: 빈 칸·비밀번호 규칙·아이디 중복 확인 후 비밀번호를 암호화해 저장
    // 빈 칸 BlankValueException, 규칙 위반 InvalidValueException, 중복 DuplicateException, 0줄 DBNotAffectedException
    public void join(JoinRequest req){

        if (req.userId() == null || req.pwd() == null || req.name() == null || req.userId().isBlank() || req.pwd().isBlank() || req.name().isBlank()){
            throw new BlankValueException(ErrorCode.JOIN_BLANK, "가입: 빈 칸");
        }

        if (!req.pwd().matches(PWD_RULE)){
            throw new InvalidValueException(ErrorCode.PWD_RULE_VIOLATION, "가입: 비밀번호 규칙 위반");
        }

        if (memberMapper.selectMemberByUserId(req.userId()) != null) {
            throw new DuplicateException(ErrorCode.USER_ID_DUPLICATE, "가입: 아이디 중복(조회)");
        }

        MemberVo memberVo = new MemberVo();
        memberVo.setUserId(req.userId());
        memberVo.setPwd(passwordEncoder.encode(req.pwd()));
        memberVo.setName(req.name());

        try {
            if(memberMapper.insertMember(memberVo) <= 0){
                throw new DBNotAffectedException(ErrorCode.SAVE_FAILED, "가입: member INSERT 0줄");
            }

        } catch (DuplicateKeyException e){
            // 중복 검사를 통과했지만 동시에 같은 아이디로 가입해 UNIQUE에 걸림
            throw new DuplicateException(ErrorCode.USER_ID_DUPLICATE, "가입: 아이디 중복(동시 가입, UNIQUE 위반)");
        }
    }
}
