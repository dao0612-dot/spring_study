package com.study.skillup.common.exception;


// INSERT/UPDATE/DELETE 결과 바뀐 행이 0일 때(변동이 없을 때) 던지는 예외
public class DBNotAffectedException extends CustomException {
    public DBNotAffectedException(ErrorCode code, String message) {
        super(code, message);
    }
}

