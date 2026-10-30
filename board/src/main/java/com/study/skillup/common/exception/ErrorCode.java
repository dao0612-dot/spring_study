package com.study.skillup.common.exception;

import org.springframework.http.HttpStatus;

// 실패 응답의 상태 코드 + 회원에게 보일 문구. 문구를 바꿀 땐 여기만 고친다
// 예외의 메시지(getMessage)는 개발자용(로그), 회원 문구는 여기에만 둔다
public enum ErrorCode {

    // 게시판
    BOARD_NOT_FOUND(HttpStatus.NOT_FOUND, "게시글을 찾을 수 없습니다."),
    BOARD_FILE_NOT_FOUND(HttpStatus.NOT_FOUND, "파일을 찾을 수 없습니다."),
    BOARD_TITLE_BLANK(HttpStatus.BAD_REQUEST, "제목을 입력해주세요."),

    // 회원
    JOIN_BLANK(HttpStatus.BAD_REQUEST, "아이디, 비밀번호, 이름을 모두 입력해주세요."),
    PWD_RULE_VIOLATION(HttpStatus.BAD_REQUEST, "비밀번호는 8자 이상, 영문과 숫자를 모두 포함해야 합니다."),
    USER_ID_DUPLICATE(HttpStatus.CONFLICT, "이미 사용 중인 아이디입니다."),

    // 공통(서버)
    SAVE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "저장이 완료되지 않았습니다. 다시 시도해주세요.");

    private final HttpStatus status;
    private final String message;

    ErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public HttpStatus getStatus() { return status; }
    public String getMessage() { return message; }
}