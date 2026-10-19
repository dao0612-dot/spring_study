package com.study.board.controller;

import com.study.board.dto.BoardWriteRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Controller
public class BoardController {

    @GetMapping("/board/list")
    public String boardList(Model model) {
        List<Map<String, Object>> list = new ArrayList<>();
        for (int i=1; i<=3; i++){
            Map<String, Object> map = new LinkedHashMap<>();

            map.put("title", i + "번째 제목");
            map.put("content", i + "번째 내용");
            list.add(map);
        }

        model.addAttribute("posts",list);

        return "board_list";
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
}
