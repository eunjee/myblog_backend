package jp.falsystack.falsylog_backend.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

@Getter
public class CommentCreate {

  @Length(min = 1, max = 8, message = "작성자는 1~8글자로 입력해주세요")
  @NotBlank(message = "작성자를 입력해주세요")
  private String author;

//  @Length(min = 8, max = 20, message = "비밀번호는 8~20글자로 입력해주세요")
  @Length(max = 20, message = "비밀번호는 촤대 20글자로 입력해주세요")
  @Length(min = 8, message = "비밀번호는 최소 8글자로 입력해주세요")
  @NotBlank(message = "비밀번호를 입력해주세요")
  private String password;

  @Length(min = 10, max = 300, message = "내용은 300자까지 입력해주세요")
  @NotBlank(message = "내용을 입력해주세요")
  private String content;

  @Builder
  public CommentCreate(String author, String password, String content) {
    this.author = author;
    this.password = password;
    this.content = content;
  }
}
