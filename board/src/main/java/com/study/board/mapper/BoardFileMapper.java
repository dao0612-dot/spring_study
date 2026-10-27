package com.study.board.mapper;

import com.study.board.vo.BoardFileVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BoardFileMapper {
    // 게시글 파일 정보 한 줄 저장, 반환 = 바뀐 행 수
    int insertBoardFile(BoardFileVo boardFileVo);

    // 글 번호로 첨부 파일 목록 조회(삭제 안 된 것만)
    List<BoardFileVo> selectBoardFileList(int boardIdx);

    // 파일 번호로 첨부 파일 조회 (파일·글 모두 삭제 안 된 것만, 없으면 null)
    BoardFileVo selectBoardFile(int fileIdx);
}
