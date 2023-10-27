package jp.falsystack.falsylog_backend.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;

@Getter
public class PostCreate {

  @NotEmpty(message = "タイトルを入力してください。")
  @Size(min = 1, max = 20, message = "タイトルは１文字以上２０文字以下で作成してください。")
  private final String title;

  @NotBlank(message = "内容を入力してください。")
  @Size(min = 10, message = "１０文字以上作成してください。")
  private final String content;
  private final String author;

  @Builder
  public PostCreate(String title, String content, String author) {
    this.title = title;
    this.content = content;
    this.author = author;
  }
}
