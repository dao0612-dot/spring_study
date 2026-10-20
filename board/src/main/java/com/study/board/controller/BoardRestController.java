package com.study.board.controller;


import com.study.board.dto.BoardWriteRequest;
import com.study.board.service.BoardService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;



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

    // 게시글 목록 반환
    @GetMapping("/api/board/list")
    public List<Map<String, Object>> getBoardList(){

        return boardService.getBoardList();
    }


    // json으로 온 작성글 저장 후 상태 코드 201+데이터 반환
    // 제목이 없으면 상태 코드 400. 제목없음 안내 문구를 반환
    @PostMapping("/api/board/write")
    public ResponseEntity<?> saveBoard(@RequestBody BoardWriteRequest req){

        try {
            boardService.saveBoard(req);
        } catch (IllegalArgumentException e) {
            Map<String, Object> wrongTitle = new LinkedHashMap<>();
            wrongTitle.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(wrongTitle);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(req);
    }
}
