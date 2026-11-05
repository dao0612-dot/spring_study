package com.study.skillup.common.exception;

// 필수 값이 비었을 때 던지는 예외
public class BlankValueException extends CustomException {
    public BlankValueException(ErrorCode code, String message) {
        super(code, message);
    }
}
