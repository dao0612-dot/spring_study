package com.study.board.mapper;

import com.study.board.vo.BoardVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BoardMapper {
    List<BoardVo> selectBoardList();

    // 반환되는 숫자 = 저장된 행의 갯수
    int insertBoard(BoardVo boardVo);

    BoardVo selectBoard(int boardIdx);

    // 글 번호의 제목·내용 수정(삭제글 제외), 반환 = 바뀐 행 수
    int updateBoard(BoardVo boardVo);

    // 글 번호의 게시글 삭제 처리, 반환 = 바뀐 행 수
    int deleteBoard(BoardVo boardVo);
}
