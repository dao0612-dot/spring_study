package com.study.board.controller;


import com.study.board.dto.BoardWriteRequest;
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
        } catch (IllegalArgumentException e) {
            // 원인이 예상이 되는 예외이므로 warn + 메세지
            log.warn("제목 없음: "+e.getMessage());
            model.addAttribute("errorMessage",e.getMessage());
            return "board_write";
        } catch (DBNotAffectedException e) {
            // 원인이 예상이 어려운 예외이므로 error + 예외객체
            log.error("DB 또는 서버 에러", e);
            model.addAttribute("errorMessage",e.getMessage());
            return "board_write";
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
