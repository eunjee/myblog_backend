package jp.falsystack.falsylog_backend.exception;

public class FileUploadFail extends MyBlogException{

  public static final String MESSAGE = "파일 업로드에 실패했습니다.";

  public FileUploadFail() {
    super(MESSAGE);
  }

  public FileUploadFail(Throwable cause) {
    super(MESSAGE, cause);
  }

  @Override
  public int getStatusCode() {
    return 500;
  }
}
