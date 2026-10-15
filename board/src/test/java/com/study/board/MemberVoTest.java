package com.study.board;

import com.study.board.VO.MemberVo;
import org.junit.jupiter.api.Test;

public class MemberVoTest {

    @Test
    public void memberVoTest(){
        MemberVo memberVo = new MemberVo();
        memberVo.setName("철수");
        System.out.println(memberVo.getName());

        System.out.println(memberVo);
    }
}
