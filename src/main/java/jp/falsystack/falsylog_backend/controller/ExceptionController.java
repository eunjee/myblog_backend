package jp.falsystack.falsylog_backend.controller;

import java.util.List;
import jp.falsystack.falsylog_backend.exception.MyBlogException;
import jp.falsystack.falsylog_backend.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@Slf4j
@RestControllerAdvice
public class ExceptionController {

  public static final String MESSAGE = "잘못된 요청입니다";

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ErrorResponse invalidRequestHandler(MethodArgumentNotValidException e) {

    var errorResponse = ErrorResponse.builder()
        .message(MESSAGE) // 間違ったリクエストです。
        .build();

    errorResponse.addValidationList(e.getFieldErrors());

    return errorResponse;
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  public ErrorResponse typeMisMatchHandler(MethodArgumentTypeMismatchException e) {

    return ErrorResponse.builder()
        .message(MESSAGE) // 間違ったリクエストです。
        .build();
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

  // 想定外のエラー
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> exception(Exception e) {
    log.error("[想定外のエラー]", e);

    var errorResponse = ErrorResponse.builder()
        .message(e.getMessage())
        .build();

    return ResponseEntity
        .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
        .body(errorResponse);
  }
}
