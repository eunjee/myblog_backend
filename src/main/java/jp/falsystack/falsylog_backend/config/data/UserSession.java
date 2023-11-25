package jp.falsystack.falsylog_backend.config.data;

import lombok.Builder;

public class UserSession {

  private final Long id;

  @Builder
  private UserSession(Long id) {
    this.id = id;
  }

  public static UserSession from(Long id) {
    return UserSession.builder().id(id).build();
  }
}
