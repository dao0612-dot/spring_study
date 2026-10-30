package com.study.skillup.common.exception;


import com.study.skillup.common.dto.ResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.UncheckedIOException;


// JSON 컨트롤러(@RestController)에서 나온 예외를 받아 상태 코드와 실패 응답을 만든다
@Slf4j
@RestControllerAdvice(annotations = RestController.class)
public class ApiExceptionHandler {

    // 우리가 던진 예외(CustomException과 그 자식 전부): 상태 코드·회원 문구는 ErrorCode에서, 예외 메시지는 로그에만
    // 4xx는 warn, 5xx는 error + 스택
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ResponseDto<Void>> handleCustom(CustomException e){
        ErrorCode code = e.getCode();
        if (code.getStatus().is5xxServerError()) {
            log.error("서버 오류({}): {}", code, e.getMessage(), e);
        } else {
            log.warn("요청 실패({}): {}", code, e.getMessage());
        }
        return ResponseEntity.status(code.getStatus())
                .body(new ResponseDto<>(code.getMessage(), null));
    }

    // 깨진 json: 400
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseDto<Void> handleNotReadable(HttpMessageNotReadableException e){
        log.warn("JSON 형식 오류: {}", e.getMessage());
        return new ResponseDto<>("요청을 처리할 수 없습니다. 새로고침 후 다시 시도해주세요.", null);
    }

    // 파일 저장 실패: 500
    @ExceptionHandler(UncheckedIOException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseDto<Void> handleUncheckdIO(UncheckedIOException e){
        log.error("파일 저장 실패: ", e);
        return new ResponseDto<>("파일 저장에 실패했습니다. 다시 시도해주세요.", null);
    }



    // 예기치 못한 오류: 500
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseDto<Void> handleException(Exception e){
        log.error("예상치 못한 오류: ", e);
        return new ResponseDto<>("예상치 못한 오류입니다.", null);
    }
}
