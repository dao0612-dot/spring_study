package com.study.skillup.common.exception;

// 우리가 직접 던지는 예외의 공통 부모. 회원용(ErrorCode)과 개발자용(메시지, 로그)을 따로 담는다
public class CustomException extends IllegalArgumentException {

    private final ErrorCode code;

    public CustomException(ErrorCode code, String message) {
        super(message);        // 개발자용: getMessage()로 로그에
        this.code = code;      // 회원용: getCode()로 응답·화면에
    }

    public ErrorCode getCode() { return code; }
}
