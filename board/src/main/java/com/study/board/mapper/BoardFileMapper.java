package com.study.board.mapper;

import com.study.board.vo.BoardFileVo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BoardFileMapper {
    // 게시글 파일 정보 한 줄 저장, 반환 = 바뀐 행 수
    int insertBoardFile(BoardFileVo boardFileVo);
}
