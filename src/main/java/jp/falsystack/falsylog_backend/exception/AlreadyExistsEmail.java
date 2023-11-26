package jp.falsystack.falsylog_backend.exception;

public class AlreadyExistsEmail extends MyBlogException{

  public static final String MESSAGE = "이미 가입된 이메일입니다."; // すでに存在するメールアドレスです。

  public AlreadyExistsEmail() {
    super(MESSAGE);
  }

  public AlreadyExistsEmail(Throwable cause) {
    super(MESSAGE, cause);
  }

  @Override
  public int getStatusCode() {
    return 400;
  }
}
