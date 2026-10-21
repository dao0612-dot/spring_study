package com.study.board.service;

import com.study.board.dto.BoardWriteRequest;
import com.study.board.exception.DBNotAffectedException;
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



    // 게시글 저장
    // 제목이 없으면 IllegalArgumentException
    // DB에 변동 사항이 없으면 DBNotAffectedException
    public void saveBoard(BoardWriteRequest req){

        String title = req.title();

        if (title == null || title.isBlank()){

            throw new IllegalArgumentException("제목을 입력하세요.");
        }


        // TODO: memberIdx: 1 임시값을 세션으로 적용
        // 0 이하면 예외, 비정상
        if (boardMapper.insertBoard(req, 1) <= 0) {

            throw new DBNotAffectedException("저장된 내용이 없습니다. 다시 시도해주세요.");
        }


    }
}
