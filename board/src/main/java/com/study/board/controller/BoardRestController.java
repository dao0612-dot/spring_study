package com.study.board.controller;


import com.study.board.dto.BoardWriteRequest;
import com.study.board.dto.ResponseDto;
import com.study.board.service.BoardService;
import com.study.board.vo.BoardVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


// 게시판 데이터 API 컨트롤러
@RestController
public class BoardRestController {

    private final BoardService boardService;

    public BoardRestController(BoardService boardService) {
        this.boardService = boardService;
    }

    // 데이터 반환 시 형태보기 연습 + Map<> 연습
    @GetMapping("/board")
    public Map<String, Object> boardRestPractice(){
        Map<String, Object> b = new LinkedHashMap<>();
        b.put("title","안녕하세요.");
        b.put("content","반갑습니다.");

        return b;
    }

    // 게시글 목록 반환 | 실패는 ApiExceptionHandler
    @GetMapping("/api/board/list")
    public ResponseDto<List<BoardVo>> getBoardList(){

        return new ResponseDto<>(null, boardService.getBoardList());
    }


    // json으로 온 작성글 저장 후 상태 코드 201+데이터 반환 | 실패는 ApiExceptionHandler
    @PostMapping("/api/board/save")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseDto<BoardWriteRequest> saveBoard(@RequestBody BoardWriteRequest req){

        boardService.saveBoard(req);
        return new ResponseDto<>(null, req);
    }
}
