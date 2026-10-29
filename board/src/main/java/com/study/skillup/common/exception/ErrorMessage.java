package com.study.skillup.common.exception;

// 사용자에게 보이는 4xx 문구 모음: 문구를 바꿀 땐 여기만 고친다(5xx는 ApiExceptionHandler의 고정 문구)
public final class ErrorMessage {

    // 상수만 두는 곳이라 객체를 만들 일이 없음
    private ErrorMessage() {}

    // 게시판
    public static final String BOARD_NOT_FOUND = "게시글을 찾을 수 없습니다.";
    public static final String BOARD_FILE_NOT_FOUND = "파일을 찾을 수 없습니다.";
    public static final String BOARD_TITLE_BLANK = "제목을 입력해주세요.";

    // 회원
    public static final String JOIN_BLANK = "아이디, 비밀번호, 이름을 모두 입력해주세요.";
    public static final String PWD_RULE_VIOLATION = "비밀번호는 8자 이상, 영문과 숫자를 모두 포함해야 합니다.";
    public static final String USER_ID_DUPLICATE = "이미 사용 중인 아이디입니다.";
}
