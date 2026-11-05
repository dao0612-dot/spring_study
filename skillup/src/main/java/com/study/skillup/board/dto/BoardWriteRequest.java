package com.study.skillup.board.dto;

// 글쓰기 요청 데이터 객체틀. form과 json 글쓰기가 같이 사용
public record BoardWriteRequest(String title, String content) {


}
