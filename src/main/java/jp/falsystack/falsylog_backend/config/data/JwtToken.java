package jp.falsystack.falsylog_backend.config.data;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

// user idを格納して送る
@Getter
public class JwtToken {

  private final Long id;

  @Builder(access = AccessLevel.PRIVATE)
  private JwtToken(Long id) {
    this.id = id;
  }

  public static JwtToken from(Long id) {
    return JwtToken.builder().id(id).build();
  }
}
