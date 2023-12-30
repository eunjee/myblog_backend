package jp.falsystack.falsylog_backend.service.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class PostEdit {
  private final String title;
  private final String content;
  private final String hashTags;

  @Builder
  public PostEdit(String title, String content, String hashTags) {
    this.title = title;
    this.content = content;
    this.hashTags = hashTags;
  }
}
