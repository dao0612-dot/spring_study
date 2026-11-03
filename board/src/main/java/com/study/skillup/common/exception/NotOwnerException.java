package com.study.skillup.common.exception;


// 다른 회원의 글을 수정, 삭제하려 할 때 예외를 던짐
public class NotOwnerException extends CustomException {
    public NotOwnerException(ErrorCode code, String message) {
        super(code, message);
    }
}
