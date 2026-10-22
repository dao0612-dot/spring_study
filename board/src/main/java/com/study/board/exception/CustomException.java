package com.study.board.exception;

// 우리가 직접 던지는 예외의 공통 부모. 자바가 던지는 예외와 구분해서 받으려고 둔다.
public class CustomException extends IllegalArgumentException {
    public CustomException(String message) {
        super(message);
    }
}
