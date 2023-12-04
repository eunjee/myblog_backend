package jp.falsystack.falsylog_backend.exception;

/**
 * Commentを見つけられない
 * status -> 404
 */
public class CommentNotFound extends MyBlogException {

  private static final String MESSAGE = "존재하지 않는 댓글입니다."; // お探しのcommentが存在しません。

  public CommentNotFound() {
    super(MESSAGE);
  }

  public CommentNotFound(Throwable cause) {
    super(MESSAGE, cause);
  }

  @Override
  public int getStatusCode() {
    return 404;
  }
}
