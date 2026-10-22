package com.study.board.controller;


import com.study.board.dto.BoardWriteRequest;
import com.study.board.exception.BlankValueException;
import com.study.board.exception.CustomException;
import com.study.board.exception.DBNotAffectedException;
import com.study.board.exception.NotFoundException;
import com.study.board.service.BoardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


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
        return "board_write";
    }


    // form으로 넘어온 작성글 저장 후 목록으로 redirect (PRG 방식)
    // 제목이 없으면 글쓰기 화면으로 돌아가 제목없음 안내 | warn 로그를 콘솔에 출력(연습용)
    // DB 변동이 없으면 재시도 안내 | error 로그를 콘솔에 출력
    @PostMapping("/board/save")
    public String saveBoard(@ModelAttribute BoardWriteRequest req, Model model){

        try {
            boardService.saveBoard(req);
        } catch (CustomException e) {
            if (e instanceof BlankValueException) {
                log.warn("필수 값 비었음: " + e.getMessage());
                model.addAttribute("errorMessage", e.getMessage());
                return "board_write";
            } else if (e instanceof DBNotAffectedException) {
                log.error("DB 변동사항 없음: ", e);
                model.addAttribute("errorMessage", e.getMessage());
                return "board_write";
            } else {
                log.error("예상치 못한 오류. 예외 처리 경로 미설정 의심", e);
                model.addAttribute("errorMessage", "예기치 못한 오류 발생");
                return "board_write";
            }
        }

        return "redirect:/board/list";
    }

    // 게시글 목록
    @GetMapping("/board/list")
    public String boardList(Model model){

        // board_list.html로 데이터를 가져간다. ${boards}가 받아쓴다.
        model.addAttribute("boards",boardService.getBoardList());

        return "board_list";
    }


    // 게시글 자세히 보기
    // 게시글을 못찾으면 목록으로 돌아간 후 안내 | warn 로그를 콘솔에 출력
    @GetMapping("/board/detail/{boardIdx}")
    public String boardDetail(@PathVariable int boardIdx, Model model, RedirectAttributes ra){
        try {
            model.addAttribute("board", boardService.getBoard(boardIdx));
        } catch (CustomException e){
            if (e instanceof NotFoundException){
                log.warn("찾을 수 없음: " + e.getMessage());
                ra.addFlashAttribute("errorMessage", e.getMessage());
                return "redirect:/board/list";
            } else {
                log.error("예상치 못한 오류. 예외 경로 미지정 의심", e);
                ra.addFlashAttribute("errorMessage", "예기치 못한 오류 발생");
                return "redirect:/board/list";
            }
        }

        return "board_detail";
    }


    // 게시글 수정 화면. 기존 제목·내용을 채워서. 글이 없으면 목록으로
    @GetMapping("/board/edit/{boardIdx}")
    public String boardEdit(@PathVariable int boardIdx, Model model, RedirectAttributes ra) {
        try {
            model.addAttribute("board", boardService.getBoard(boardIdx));
        } catch (CustomException e) {
            if (e instanceof NotFoundException) {
                log.warn("해당되는 게시글이 없음"+e.getMessage());
                ra.addFlashAttribute("errorMessage", e.getMessage());
                return "redirect:/board/list";
            } else {
                log.error("예상치 못한 오류. 예외 경로 미지정 의심", e);
                ra.addFlashAttribute("errorMessage", "예기치 못한 오류 발생");
                return "redirect:/board/detail/{boardIdx}";
            }
        }
        return "board_edit";
    }


    // 게시글 수정하기
    @PostMapping("/board/update/{boardIdx}")
    public String updateBoard(@PathVariable int boardIdx, @ModelAttribute BoardWriteRequest req, RedirectAttributes ra){
        try{
            boardService.updateBoard(boardIdx, req);
        } catch (CustomException e){
            if (e instanceof BlankValueException){
                log.warn("필수 값 비었음: "+e.getMessage());
                ra.addFlashAttribute("errorMessage", e.getMessage());
                return "redirect:/board/edit/{boardIdx}";
            } else if (e instanceof NotFoundException){
                log.warn("해당되는 게시글 없음"+e.getMessage());
                ra.addFlashAttribute("errorMessage", e.getMessage());
                return "redirect:/board/list";
            } else {
                log.error("예상치 못한 오류. 예외 처리 경로 미지정 의심", e);
                ra.addFlashAttribute("errorMessage", "예기치 못한 오류 발생");
                return "redirect:/board/edit/{boardIdx}";
            }
        }
        return "redirect:/board/detail/{boardIdx}";
    }
}
