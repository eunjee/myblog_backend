package jp.falsystack.falsylog_backend.exception;

/**
 * ポストを見つけられない
 * status -> 404
 */
public class PostNotFound extends MyBlogException {

  private static final String MESSAGE = "찾으시는 게시글이 없습니다.";

  public PostNotFound() {
    super(MESSAGE);
  }

  public PostNotFound(Throwable cause) {
    super(MESSAGE, cause);
  }

  @Override
  public int getStatusCode() {
    return 404;
  }
}
