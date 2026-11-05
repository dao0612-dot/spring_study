package com.study.skillup.board.vo;


import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

// board 테이블 행을 객체로
@Getter
@Setter

public class BoardVo {
    private Integer boardIdx;
    private String title;
    private String content;
    private Integer memberIdx;
    private String delYn;
    private LocalDateTime regDttm;
    private Integer uptMemberIdx;
    private LocalDateTime uptDttm;
    private Integer delMemberIdx;
    private LocalDateTime delDttm;
}
