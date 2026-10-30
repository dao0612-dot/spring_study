package com.study.skillup.board.controller;


import com.study.skillup.board.dto.BoardWriteRequest;
import com.study.skillup.common.exception.BlankValueException;
import com.study.skillup.common.exception.CustomException;
import com.study.skillup.common.exception.DBNotAffectedException;
import com.study.skillup.common.exception.NotFoundException;
import com.study.skillup.common.file.DownloadFile;
import com.study.skillup.board.service.BoardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;


// 게시판 화면 렌더링용 컨트롤러
@Slf4j
@Controller
public class BoardController {

    private final BoardService boardService;
    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    // 글쓰기 화면
    @GetMapping("/board/write")
    public String boardWrite(){
        return "board/write";
    }


    // (예비 경로) 글쓰기 화면은 JS(fetch)로 /api/board/save에 저장한다. 이 메서드는 JS가 안 돌 때만 쓰인다.
    // form으로 넘어온 작성글 저장 후 목록으로 redirect (PRG 방식)
    // 제목이 없으면 글쓰기 화면으로 돌아가 제목없음 안내 | warn 로그를 콘솔에 출력(연습용)
    // DB 변동이 없으면 재시도 안내 | error 로그를 콘솔에 출력
    // 파일이 있으면 함께 저장, 파일 저장 실패 시 글쓰기 화면 + 안내
    @PostMapping("/board/save")
    public String saveBoard(@ModelAttribute BoardWriteRequest req, Model model, @RequestParam(value = "file", required = false) MultipartFile file){

        try {
            boardService.saveBoard(req, file);
        } catch (CustomException e) {
            if (e instanceof BlankValueException) {
                log.warn("필수 값 비었음: " + e.getMessage());
                model.addAttribute("errorMessage", e.getCode().getMessage());
                return "board/write";
            } else if (e instanceof DBNotAffectedException) {
                log.error("DB 변동사항 없음: ", e);
                model.addAttribute("errorMessage", e.getCode().getMessage());
                return "board/write";
            } else {
                log.error("예상치 못한 오류. 예외 처리 경로 미설정 의심", e);
                model.addAttribute("errorMessage", "예기치 못한 오류 발생");
                return "board/write";
            }
        } catch (UncheckedIOException e) {
            log.error("파일 저장에 실패했습니다. 다시 시도해주세요.", e);
            model.addAttribute("errorMessage","파일 저장에 실패했습니다. 다시 시도해주세요");
            return "board/write";
        }

        return "redirect:/board/list";
    }

    // 게시글 목록
    @GetMapping("/board/list")
    public String boardList(Model model){

        // board_list.html로 데이터를 가져간다. ${boards}가 받아쓴다.
        model.addAttribute("boards",boardService.getBoardList());

        return "board/list";
    }


    // 게시글 자세히 보기
    // 게시글을 못찾으면 목록으로 돌아간 후 안내 | warn 로그를 콘솔에 출력
    @GetMapping("/board/detail/{boardIdx}")
    public String boardDetail(@PathVariable int boardIdx, Model model, RedirectAttributes ra){
        try {
            model.addAttribute("board", boardService.getBoard(boardIdx));
            model.addAttribute("files", boardService.getBoardFileList(boardIdx));
        } catch (CustomException e){
            if (e instanceof NotFoundException){
                log.warn("찾을 수 없음: " + e.getMessage());
                ra.addFlashAttribute("errorMessage", e.getCode().getMessage());
                return "redirect:/board/list";
            } else {
                log.error("예상치 못한 오류. 예외 경로 미지정 의심", e);
                ra.addFlashAttribute("errorMessage", "예기치 못한 오류 발생");
                return "redirect:/board/list";
            }
        }

        return "board/detail";
    }


    // 게시글 수정 화면. 기존 제목·내용을 채워서. 글이 없으면 목록으로
    @GetMapping("/board/edit/{boardIdx}")
    public String boardEdit(@PathVariable int boardIdx, Model model, RedirectAttributes ra) {
        try {
            model.addAttribute("board", boardService.getBoard(boardIdx));
        } catch (CustomException e) {
            if (e instanceof NotFoundException) {
                log.warn("해당되는 게시글이 없음: "+e.getMessage());
                ra.addFlashAttribute("errorMessage", e.getCode().getMessage());
                return "redirect:/board/list";
            } else {
                log.error("예상치 못한 오류. 예외 경로 미지정 의심", e);
                ra.addFlashAttribute("errorMessage", "예기치 못한 오류 발생");
                return "redirect:/board/detail/{boardIdx}";
            }
        }
        return "board/edit";
    }


    // 게시글 수정하기
    @PostMapping("/board/update/{boardIdx}")
    public String updateBoard(@PathVariable int boardIdx, @ModelAttribute BoardWriteRequest req, RedirectAttributes ra){
        try{
            boardService.updateBoard(boardIdx, req);
        } catch (CustomException e){
            if (e instanceof BlankValueException){
                log.warn("필수 값 비었음: "+e.getMessage());
                ra.addFlashAttribute("errorMessage", e.getCode().getMessage());
                return "redirect:/board/edit/{boardIdx}";
            } else if (e instanceof NotFoundException){
                log.warn("해당되는 게시글 없음: "+e.getMessage());
                ra.addFlashAttribute("errorMessage", e.getCode().getMessage());
                return "redirect:/board/list";
            } else {
                log.error("예상치 못한 오류. 예외 처리 경로 미지정 의심", e);
                ra.addFlashAttribute("errorMessage", "예기치 못한 오류 발생");
                return "redirect:/board/edit/{boardIdx}";
            }
        }
        return "redirect:/board/detail/{boardIdx}";
    }


    //게시글 삭제하기
    @PostMapping("/board/delete/{boardIdx}")
    public String deleteBoard(@PathVariable int boardIdx, RedirectAttributes ra){

        try {
            boardService.deleteBoard(boardIdx);
        } catch (CustomException e) {
            if (e instanceof NotFoundException) {
                log.warn("해당되는 게시글 없음: " + e.getMessage());
                ra.addFlashAttribute("errorMessage", e.getCode().getMessage());
                return "redirect:/board/list";
            } else {
                log.error("예상치 못한 오류. 예외 처리 미지정 의심", e);
                ra.addFlashAttribute("errorMessage", "예기치 못한 오류 발생");
                return "redirect:/board/list";

            }
        }

        return "redirect:/board/list";

    }


    // 게시글 목록(fetch 버전)
    @GetMapping("/board/fetch")
    public String boardFetch(){

        return "board/fetch";
    }

    // 첨부 파일 내려받기: 헤더에 원래 이름을 붙여 파일로 보낸다
    // 파일 정보가 없거나 디스크에 없으면 404 | warn 로그
    @GetMapping("/board/file/download/{fileIdx}")
    public ResponseEntity<Resource> downloadBoardFile(@PathVariable int fileIdx){
        DownloadFile file;
        try{
            file = boardService.getBoardFile(fileIdx);
        } catch (NotFoundException e){
            log.warn("내려받을 파일 없음: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }

        ContentDisposition disposition = ContentDisposition.attachment()
                .filename(file.originalName(), StandardCharsets.UTF_8)
                .build();
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, disposition.toString())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(file.resource());
    }
}
