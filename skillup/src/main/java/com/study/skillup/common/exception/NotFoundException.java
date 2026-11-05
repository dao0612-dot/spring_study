package com.study.skillup.common.exception;


// 입력한 값에 대한 결과가 없을 때 예외를 던짐
public class NotFoundException extends CustomException {
    public NotFoundException(ErrorCode code, String message) {
        super(code, message);
    }
}
