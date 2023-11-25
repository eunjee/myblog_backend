package jp.falsystack.falsylog_backend.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;

@Getter
public class Login {

  @Email(message = "이메일 형식에 맞지 않습니다")
  @NotBlank(message = "이메일을 입력해주세요")
  private String email;
  @Size(message = "8자리 이상 16자리 이하로 입력해주세요", min = 8, max = 16)
  @NotBlank(message = "패스워드를 입력해주세요")
  private String password;

  @Builder
  private Login(String email, String password) {
    this.email = email;
    this.password = password;
  }

  public static Login of(String email, String password) {
    return Login.builder()
        .email(email)
        .password(password
        ).build();
  }
}
