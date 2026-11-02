package com.study.skillup.board.controller;


import com.study.skillup.board.dto.BoardWriteRequest;
import com.study.skillup.common.dto.ResponseDto;
import com.study.skillup.board.service.BoardService;
import com.study.skillup.board.vo.BoardVo;
import com.study.skillup.member.security.LoginMember;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


// 게시판 데이터 API 컨트롤러
@RestController
public class BoardRestController {

    private final BoardService boardService;

    public BoardRestController(BoardService boardService) {
        this.boardService = boardService;
    }

    // 게시글 목록 반환 | 실패는 ApiExceptionHandler
    @GetMapping("/api/board/list")
    public ResponseDto<List<BoardVo>> getBoardList(){

        return new ResponseDto<>(null, boardService.getBoardList());
    }


    // 폼 데이터로 온 작성글, 파일(있다면) 저장 후 상태 코드 201+데이터 반환 | 실패는 ApiExceptionHandler
    @PostMapping("/api/board/save")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseDto<BoardWriteRequest> saveBoard(@ModelAttribute BoardWriteRequest req, @RequestParam(value = "file", required = false) MultipartFile file, @AuthenticationPrincipal LoginMember loginMember){

        boardService.saveBoard(req, file, loginMember.getMemberIdx());
        return new ResponseDto<>(null, req);
    }
}
