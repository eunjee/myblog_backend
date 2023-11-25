package jp.falsystack.falsylog_backend.exception;

public class InvalidSigninInformation extends MyBlogException{

  public static final String MESSAGE = "아이디 또는 패스워드를 확인해 주세요";

  public InvalidSigninInformation() {
    super(MESSAGE);
  }

  public InvalidSigninInformation(Throwable cause) {
    super(MESSAGE, cause);
  }

  @Override
  public int getStatusCode() {
    return 400;
  }
}
