package jp.falsystack.falsylog_backend.exception;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.Getter;
import org.springframework.validation.FieldError;

// ビジネス最上位例外
@Getter
public abstract class MyBlogException extends RuntimeException{

  private final Map<String, String> validation = new ConcurrentHashMap<>();

  public MyBlogException(String message) {
    super(message);
  }

  public MyBlogException(String message, Throwable cause) {
    super(message, cause);
  }

  public void addEachValidation(List<FieldError> fieldErrors) {
    for (FieldError fieldError : fieldErrors) {
      this.validation.put(fieldError.getField(), fieldError.getDefaultMessage());
    }
  }

  public abstract int getStatusCode();
}
