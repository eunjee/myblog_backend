package jp.falsystack.falsylog_backend.controller;

import jp.falsystack.falsylog_backend.response.ErrorResponse;
import org.springframework.http.HttpStatus;
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
        .message("間違ったリクエストです。")
        .build();

    errorResponse.addValidation(e.getFieldErrors());

    return errorResponse;
  }
}
