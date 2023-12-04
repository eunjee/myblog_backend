package jp.falsystack.falsylog_backend.exception;

/**
 * メンバーを見つけられない
 * status -> 404
 */
public class MemberNotFound extends MyBlogException {

  private static final String MESSAGE = "존재하지 않는 사용자입니다."; // お探しのmemberが存在しません。

  public MemberNotFound() {
    super(MESSAGE);
  }

  public MemberNotFound(Throwable cause) {
    super(MESSAGE, cause);
  }

  @Override
  public int getStatusCode() {
    return 404;
  }
}
