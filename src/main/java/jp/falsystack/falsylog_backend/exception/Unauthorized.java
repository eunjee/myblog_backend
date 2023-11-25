package jp.falsystack.falsylog_backend.exception;

public class Unauthorized extends MyBlogException {

  private static String MESSAGE = "인증이 필요합니다."; // 認証が必要です。

  public Unauthorized() {
    super(MESSAGE);
  }

  public Unauthorized(Throwable cause) {
    super(MESSAGE, cause);
  }

  @Override
  public int getStatusCode() {
    return 401;
  }
}
