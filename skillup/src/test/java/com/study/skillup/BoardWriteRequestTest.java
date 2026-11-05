package com.study.skillup;

import com.study.skillup.board.dto.BoardWriteRequest;
import org.junit.jupiter.api.Test;



public class BoardWriteRequestTest {

    @Test
    public void boardWriteRequestTest(){
        BoardWriteRequest req = new BoardWriteRequest("제목1","내용1");


        System.out.println(req.title());
        System.out.println(req);
    }
}
