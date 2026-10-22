package com.study.board.mapper;

import com.study.board.vo.BoardVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface BoardMapper {
    List<Map<String, Object>> selectBoardList();
    // 반환되는 숫자 = 저장된 행의 갯수
    int insertBoard(BoardVo boardVo);
}
