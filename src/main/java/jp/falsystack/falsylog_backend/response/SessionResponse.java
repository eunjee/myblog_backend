package jp.falsystack.falsylog_backend.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SessionResponse {

  private String accessToken;

  public static SessionResponse from(String accessToken) {
    return new SessionResponse(accessToken);
  }
}
