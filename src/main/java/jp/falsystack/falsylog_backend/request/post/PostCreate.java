package jp.falsystack.falsylog_backend.request.post;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;

@Getter
public class PostCreate {

  @NotBlank(message = "제목을 입력해주세요") // タイトルを入力してください。
  @Size(min = 1, max = 20, message = "제목은 1~20 글자로 작성해 주세요") // タイトルは１文字以上２０文字以下で作成してください。
  private final String title;

  @NotBlank(message = "내용을 입력해주세요") // 内容を入力してください。
  @Size(min = 10, message = "10글자 이상 입력해주세요") // １０文字以上入力してください。
  private final String content;
  private final String hashTags;

  @Builder
  public PostCreate(String title, String content, String hashTags) {
    this.title = title;
    this.content = content;
    this.hashTags = hashTags;
  }
}
