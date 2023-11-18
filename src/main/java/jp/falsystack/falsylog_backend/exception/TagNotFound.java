package jp.falsystack.falsylog_backend.exception;

/**
 * hashtagを見つけられない
 * status -> 404
 */
public class TagNotFound extends MyBlogException {

  private static final String MESSAGE = "찾으시는 해시태그가 없습니다.";

  public TagNotFound() {
    super(MESSAGE);
  }

  public TagNotFound(Throwable cause) {
    super(MESSAGE, cause);
  }

  @Override
  public int getStatusCode() {
    return 404;
  }
}
