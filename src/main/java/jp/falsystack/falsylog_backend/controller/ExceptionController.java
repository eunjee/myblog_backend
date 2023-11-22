package jp.falsystack.falsylog_backend.controller;

import jp.falsystack.falsylog_backend.exception.MyBlogException;
import jp.falsystack.falsylog_backend.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionController {

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ErrorResponse invalidRequestHandler(MethodArgumentNotValidException e) {

    var errorResponse = ErrorResponse.builder()
        .message("잘못된 요청입니다.") // 間違ったリクエストです。
        .build();

    errorResponse.addValidationList(e.getFieldErrors());

    return errorResponse;
  }

  @ExceptionHandler(MyBlogException.class)
  public ResponseEntity<ErrorResponse> myBlogExceptionHandler(MyBlogException e) {

    var errorResponse = ErrorResponse.builder()
        .message(e.getMessage())
        .validation(e.getValidation())
        .build();

    return ResponseEntity
        .status(e.getStatusCode())
        .body(errorResponse);
  }
}
