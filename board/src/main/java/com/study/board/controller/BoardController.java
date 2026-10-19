package com.study.board.controller;


import com.study.board.dto.BoardWriteRequest;
import com.study.board.service.BoardService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class BoardController {

    private final BoardService boardService;

    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }


    @GetMapping("/board/post")
    public String boardPost(){
        return "board_post";
    }

    @PostMapping("/board/post")
    public String boardPostSave(@ModelAttribute BoardWriteRequest req, @RequestParam("username") String username){
        System.out.println(req.title());
        System.out.println(req.content());
        System.out.println(username);
        return "redirect:/board/list";
    }

    @GetMapping("/board/list")
    public String boardList(Model model){

        model.addAttribute("posts",boardService.getBoardList());

        return "board_list";
    }
}
