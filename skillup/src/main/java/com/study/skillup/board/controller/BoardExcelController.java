package com.study.skillup.board.controller;

import com.study.skillup.board.service.BoardExcelService;
import com.study.skillup.common.dto.ResponseDto;
import com.study.skillup.member.security.LoginMember;
import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;

// 게시판 엑셀 다운로드 / 업로드
@RestController
public class BoardExcelController {

    private static final MediaType XLSX = MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

    private final BoardExcelService boardExcelService;
    public BoardExcelController(BoardExcelService boardExcelService) {
        this.boardExcelService = boardExcelService;
    }

    // 목록 엑셀 받기: 파일 이름을 헤더에 붙여 바이트로 보낸다
    @GetMapping("/board/excel/download")
    public ResponseEntity<byte[]> downloadBoardExcel() {
        byte[] excel = boardExcelService.createBoardListExcel();

        ContentDisposition disposition = ContentDisposition.attachment()
                .filename("게시글목록_" + LocalDate.now() + ".xlsx", StandardCharsets.UTF_8)
                .build();
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, disposition.toString())
                .contentType(XLSX)
                .body(excel);
    }

    // 엑셀 업로드: 파일 내용을 토대로 게시글을 작성한다.
    @PostMapping("/api/board/excel/upload")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseDto<Integer> uploadBoardExcel(@RequestParam("file") MultipartFile file, @AuthenticationPrincipal LoginMember loginMember) {
        int excelCnt =  boardExcelService.uploadBoardExcel(file, loginMember.getMemberIdx());
        return new ResponseDto<>(null, excelCnt);
    }
}
