package com.study.board.controller;


import com.study.board.dto.BoardWriteRequest;
import com.study.board.dto.ResponseDto;
import com.study.board.exception.BlankValueException;
import com.study.board.exception.CustomException;
import com.study.board.exception.DBNotAffectedException;
import com.study.board.service.BoardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;



// 게시판 데이터 API 컨트롤러
@Slf4j
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
    // 제목이 없으면 상태 코드 400. 제목없음 안내 문구를 반환 | warn 로그를 콘솔에 출력 (연습용)
    // DB 변동이 없으면 상태 코드 500. 재시도 안내 문구를 반환 | error 로그를 콘솔에 출력
    @PostMapping("/api/board/save")
    public ResponseEntity<ResponseDto<BoardWriteRequest>> saveBoard(@RequestBody BoardWriteRequest req){

        try {
            boardService.saveBoard(req);
        } catch (CustomException e) {
            if (e instanceof BlankValueException) {
                log.warn("필수 값 비었음: " + e.getMessage());
                return ResponseEntity.badRequest().body(new ResponseDto<>(e.getMessage(), null));
            } else if (e instanceof DBNotAffectedException){
                log.error("DB 변동사항 없음: ", e);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseDto<>(e.getMessage(), null));
            } else {
                log.error("예상치 못한 오류. 예외 처리 경로 미설정 의심", e);
                return ResponseEntity.badRequest().body(new ResponseDto<>(e.getMessage(),null));
            }
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseDto<>(null, req));
    }
}
