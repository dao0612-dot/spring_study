package com.study.board.exception;

// 필수 값이 생겼을 때 던지는 예외
public class BlankValueException extends CustomException {
    public BlankValueException(String message) {
        super(message);
    }
}
