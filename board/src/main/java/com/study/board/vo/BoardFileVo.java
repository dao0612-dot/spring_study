package com.study.board.vo;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

// board_file 테이블 행을 객체로
@Getter
@Setter
public class BoardFileVo {
    private Integer fileIdx;
    private Integer boardIdx;
    private String fileOriginalName;
    private String fileSaveName;
    private Long fileSize;
    private String delYn;
    private LocalDateTime regDttm;
    private LocalDateTime delDttm;
}
