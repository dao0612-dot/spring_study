package com.study.skillup.common.exception;

// 값은 있는데 규칙에 안 맞을 때(예: 비밀번호 규칙)
public class InvalidValueException extends CustomException {
    public InvalidValueException(ErrorCode code, String message) {
        super(code, message);
    }
}
