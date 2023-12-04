package jp.falsystack.falsylog_backend.exception;

public class InvalidPassword extends MyBlogException{

  public static final String MESSAGE = "패스워드를 확인해 주세요";

  public InvalidPassword() {
    super(MESSAGE);
  }

  public InvalidPassword(Throwable cause) {
    super(MESSAGE, cause);
  }

  @Override
  public int getStatusCode() {
    return 400;
  }
}
