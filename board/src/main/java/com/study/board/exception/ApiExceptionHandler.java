package com.study.board.exception;


import com.study.board.dto.ResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;


// JSON 컨트롤러(@RestController)에서 나온 예외를 받아 상태 코드와 실패 응답을 만든다
@Slf4j
@RestControllerAdvice(annotations = RestController.class)
public class ApiExceptionHandler {

    // 대상이 없을 때: 404
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ResponseDto<Void>> handleNotFound(NotFoundException e){
        log.warn("찾을 수 없음: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseDto<>("게시글을 찾을 수 없습니다.", null));
    }

    // 필수 값이 없을 때: 400
    @ExceptionHandler(BlankValueException.class)
    public ResponseEntity<ResponseDto<Void>> handleBlankValue(BlankValueException e){
        log.warn("필수 값 누락: {}", e.getMessage());
        return ResponseEntity.badRequest().body(new ResponseDto<>("제목을 넣어주세요.", null));
    }

    // insert query가 정상적으로 돌았지만 바뀐 행이 없을 때: 500
    @ExceptionHandler(DBNotAffectedException.class)
    public ResponseEntity<ResponseDto<Void>> handleDBNotAffected(DBNotAffectedException e){
        log.error("insert 쿼리 모순 발생(처리 정상, 바뀐 행 없음)", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseDto<>("저장이 완료되지 않았습니다. 다시 시도해주세요.", null));
    }

    // 직접 지정되지 않은 예외: 400
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ResponseDto<Void>> handleCustomException(CustomException e){
        log.error("직접 지정되지 않은 예외", e);
        return ResponseEntity.badRequest().body(new ResponseDto<>("예상치 못한 오류입니다.", null));
    }

    // 예기치 못한 오류: 500
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseDto<Void>> handleException(Exception e){
        log.error("예상치 못한 오류", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseDto<>("예상치 못한 오류입니다.", null));
    }
}
