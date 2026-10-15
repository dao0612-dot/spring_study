package com.study.board.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
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

            map.put("제목", i + "번째 제목");
            map.put("내용", i + "번째 내용");
            map.put("추천수", i);
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
    public String boardPostadd(@RequestParam("title") String title,@RequestParam("content") String content,@RequestParam("username") String username){
        System.out.println(title);
        System.out.println(content);
        System.out.println(username);
        return "redirect:/board/list";
    }
}
