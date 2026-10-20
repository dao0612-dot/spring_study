package com.study.board.service;

import com.study.board.dto.BoardWriteRequest;
import com.study.board.mapper.BoardMapper;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
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



    // 게시글 저장
    // 제목이 없으면 IllegalArgumentException
    public void saveBoard(BoardWriteRequest req){

        String title = req.title();

        if (title == null || title.isBlank()){

            throw new IllegalArgumentException("제목을 입력하세요.");
        }

        Map<String, Object> board = new LinkedHashMap<>();

        board.put("title",title);
        board.put("content",req.content());
        // TODO: memberIdx 로그인 회원 번호로 변경
        board.put("memberIdx", 1);

        boardMapper.insertBoard(board);
    }
}
