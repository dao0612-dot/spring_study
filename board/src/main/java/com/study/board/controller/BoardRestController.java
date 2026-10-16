package com.study.board.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
public class BoardRestController {

    @GetMapping("/board")
    public Map<String, Object> board(){
        Map<String, Object> b = new LinkedHashMap<>();
        b.put("title","안녕하세요.");
        b.put("content","반갑습니다.");

        return b;
    }


    @GetMapping("/api/board/list")
    public List<Map<String, Object>> list() {
        List<Map<String, Object>> list = new ArrayList<>();
//        Map<String, Object> map1 = new LinkedHashMap<>();
//        map1.put("제목","안녕하세요");
//        map1.put("내용","반갑습니다");
//        map1.put("추천 수", 6);
//        list.add(map1);
//
//        Map<String, Object> map2 = new LinkedHashMap<>();
//        map2.put("제목","잘 부탁드려요");
//        map2.put("내용","열심히 해봐요");
//        map2.put("추천 수", 10);
//        list.add(map2);

//        Map<String, Object> map = new LinkedHashMap<>();
        for (int i=1; i<=3; i++){
            Map<String, Object> map = new LinkedHashMap<>();

            map.put("title", i + "번째 제목");
            map.put("content", i + "번째 내용");
            list.add(map);
        }

        for (Map<String, Object> post : list) {
            System.out.println(post.get("title") + " 제목 숫자");
        }

        return list;
    }
}
