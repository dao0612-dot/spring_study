package com.study.board.controller;


import com.study.board.dto.BoardWriteRequest;
import com.study.board.exception.BlankValueException;
import com.study.board.exception.CustomException;
import com.study.board.exception.DBNotAffectedException;
import com.study.board.service.BoardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;



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
                model.addAttribute("errorMessage",e.getMessage());
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
}
