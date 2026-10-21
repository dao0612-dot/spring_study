package com.study.board.exception;


// INSERT/UPDATE/DELETE 결과 바뀐 행이 0일 때 던지는 예외
public class DBNotAffectedException extends RuntimeException {
    public DBNotAffectedException(String message) {
        super(message);
    }
}
