package jp.falsystack.falsylog_backend.request.post;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jp.falsystack.falsylog_backend.service.dto.PostEdit;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Builder
public class PostUpdate {

  @NotBlank(message = "제목을 입력해주세요") // タイトルを入力してください。
  @Size(min = 1, max = 30, message = "제목은 1~30 글자로 작성해 주세요") // タイトルは1文字以上30文字以下で作成してください。
  private String title;

  @NotBlank(message = "내용을 입력해주세요") // 内容を入力してください。
  @Size(min = 10, message = "10글자 이상 입력해주세요") // １０文字以上入力してください。
  private String content;
  private String hashTags;

  @Builder
  public PostUpdate(String title, String content, String hashTags) {
    this.title = title;
    this.content = content;
    this.hashTags = hashTags;
  }

  public PostEdit toPostEdit() {
    return PostEdit.builder()
        .title(this.title)
        .content(this.content)
        .hashTags(this.hashTags)
        .build();
  }

}
