package com.study.skillup.member.mapper;

import com.study.skillup.member.vo.MemberVo;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface MemberMapper {

    //  유저 아이디로 계정 찾기
    MemberVo selectMemberByUserId(String userId);

    // 회원가입
    int insertMember(MemberVo memberVo);
}
