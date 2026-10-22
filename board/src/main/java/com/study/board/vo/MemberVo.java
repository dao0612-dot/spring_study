package com.study.board.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;


// member 테이블 행을 객체로
@Getter
@Setter
@ToString(exclude = "pwd")

public class MemberVo {
    private Integer memberIdx;
    private String userId;
    private String pwd;
    private String name;
    private String role;
    private String useYn;
    private LocalDateTime regDttm;
    private LocalDateTime uptDttm;
    private LocalDateTime delDttm;
}
