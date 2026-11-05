package com.study.skillup.board.controller;


import com.study.skillup.board.service.BoardService;
import com.study.skillup.common.exception.CustomException;
import com.study.skillup.common.exception.NotFoundException;
import com.study.skillup.common.exception.NotOwnerException;
import com.study.skillup.common.file.DownloadFile;
import com.study.skillup.member.security.LoginMember;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.nio.charset.StandardCharsets;


// 게시판 화면 렌더링용 컨트롤러
@Slf4j
@Controller
public class BoardController {

    private final BoardService boardService;
    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    // 글쓰기 화면(수정과 html 공유 form.html)
    @GetMapping("/board/write")
    public String boardWrite(){
        return "board/form";
    }


    // 게시글 목록
    @GetMapping("/board/list")
    public String boardList(Model model){

        // board/list.html로 데이터를 가져간다. ${boards}가 받아쓴다.
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


    // 게시글 수정 화면. 기존 제목·내용을 채워서. 글이 없으면 목록으로(글쓰기와 html 공유 form.html)
    @GetMapping("/board/edit/{boardIdx}")
    public String boardEdit(@PathVariable int boardIdx, Model model, RedirectAttributes ra, @AuthenticationPrincipal LoginMember loginMember) {
        try {
            model.addAttribute("board", boardService.getBoardForEdit(boardIdx, loginMember.getMemberIdx()));
        } catch (CustomException e) {
            if (e instanceof NotFoundException) {
                log.warn("해당되는 게시글이 없음: "+e.getMessage());
                ra.addFlashAttribute("errorMessage", e.getCode().getMessage());
                return "redirect:/board/list";
            } else if(e instanceof NotOwnerException) {
              log.warn("다른 작성자의 게시글 수정 시도: "+e.getMessage());
              ra.addFlashAttribute("errorMessage", e.getCode().getMessage());
              return "redirect:/board/detail/{boardIdx}";
            } else {
                log.error("예상치 못한 오류. 예외 경로 미지정 의심", e);
                ra.addFlashAttribute("errorMessage", "예기치 못한 오류 발생");
                return "redirect:/board/detail/{boardIdx}";
            }
        }
        return "board/form";
    }


    //게시글 삭제하기
    @PostMapping("/board/delete/{boardIdx}")
    public String deleteBoard(@PathVariable int boardIdx, RedirectAttributes ra, @AuthenticationPrincipal LoginMember loginMember){

        try {
            boardService.deleteBoard(boardIdx, loginMember.getMemberIdx());
        } catch (CustomException e) {
            if (e instanceof NotFoundException) {
                log.warn("해당되는 게시글 없음: " + e.getMessage());
                ra.addFlashAttribute("errorMessage", e.getCode().getMessage());
                return "redirect:/board/list";
            } else if (e instanceof NotOwnerException) {
                log.warn("다른 작성자의 게시글 삭제 시도: "+e.getMessage());
                ra.addFlashAttribute("errorMessage", e.getCode().getMessage());
                return "redirect:/board/detail/{boardIdx}";
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
