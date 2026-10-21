package com.study.board.mapper;

import com.study.board.dto.BoardWriteRequest;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface BoardMapper {
    List<Map<String, Object>> selectBoardList();
    // 반환되는 숫자 = 저장된 행의 갯수
    int insertBoard(@Param("board") BoardWriteRequest req, @Param("memberIdx") int memberIdx);
}
