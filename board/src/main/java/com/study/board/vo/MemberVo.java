package com.study.board.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;



// TODO: member 테이블 설계와 맞게 필드명 재작성
@Getter
@Setter
@ToString(exclude = "password")

public class MemberVo {
    private Integer id;
    private String name;
    private String userId;
    private String password;
    private String role;
    private Boolean active;
}
