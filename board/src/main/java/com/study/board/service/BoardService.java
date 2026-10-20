package com.study.board.service;

import com.study.board.mapper.BoardMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class BoardService {

    private final BoardMapper boardMapper;

    public BoardService(BoardMapper boardMapper) {
        this.boardMapper = boardMapper;
    }

    // 삭제글 제외한 게시글 목록 조회
    public List<Map<String, Object>> getBoardList(){
        return boardMapper.selectBoardList();
    }
}
