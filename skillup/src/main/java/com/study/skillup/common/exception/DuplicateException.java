package com.study.skillup.common.exception;

// 이미 있는 값과 겹칠 때(예: 아이디 중복)
public class DuplicateException extends CustomException {
    public DuplicateException(ErrorCode code, String message) {
        super(code, message);
    }
}
