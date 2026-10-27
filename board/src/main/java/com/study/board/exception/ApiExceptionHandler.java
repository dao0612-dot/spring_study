package com.study.board.exception;


import com.study.board.dto.ResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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

    // 대상이 없을 때: 404
    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseDto<Void> handleNotFound(NotFoundException e){
        log.warn("찾을 수 없음: {}", e.getMessage());
        return new ResponseDto<>("게시글을 찾을 수 없습니다.", null);
    }

    // 필수 값이 없을 때: 400
    @ExceptionHandler(BlankValueException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseDto<Void> handleBlankValue(BlankValueException e){
        log.warn("필수 값 누락: {}", e.getMessage());
        return new ResponseDto<>("제목을 넣어주세요.", null);
    }

    // insert query가 정상적으로 돌았지만 바뀐 행이 없을 때: 500
    @ExceptionHandler(DBNotAffectedException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseDto<Void> handleDBNotAffected(DBNotAffectedException e){
        log.error("insert 쿼리 모순 발생(처리 정상, 바뀐 행 없음)", e);
        return new ResponseDto<>("저장이 완료되지 않았습니다. 다시 시도해주세요.", null);
    }

    // 직접 지정되지 않은 예외: 400
    @ExceptionHandler(CustomException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseDto<Void> handleCustom(CustomException e){
        log.error("직접 지정되지 않은 예외", e);
        return new ResponseDto<>("예상치 못한 오류입니다.", null);
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
