package com.study.board.controller;


import com.study.board.dto.BoardWriteRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/api/board/post")
    public ResponseEntity<?> postAdd(@RequestBody BoardWriteRequest req){
        System.out.println(req.title());
        System.out.println(req.content());

        String title = req.title();

        if (title==null || title.isBlank()){

            Map<String, Object> wrongTitle = new LinkedHashMap<>();
            wrongTitle.put("message","제목을 입력하세요");

            return ResponseEntity.badRequest().body(wrongTitle);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(req);
    }
}
