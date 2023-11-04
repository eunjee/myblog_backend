package jp.falsystack.falsylog_backend.response;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Builder;
import lombok.Getter;
import org.springframework.validation.FieldError;

/**
 * {
 *  "message": "잘못된 요청입니다.",
 *  "validation" : {
 *    "title": "1글자 입력해주세요."
 *    }
 *  }
 */

@Getter
public class ErrorResponse {

  private final String message;
  private final Map<String, String> validation = new HashMap<>();

  public void addValidation(List<FieldError> fieldErrors) {
    for (FieldError fieldError : fieldErrors) {
      this.validation.put(fieldError.getField(), fieldError.getDefaultMessage());
    }
  }

  @Builder
  public ErrorResponse(String message) {
    this.message = message;
  }
}
