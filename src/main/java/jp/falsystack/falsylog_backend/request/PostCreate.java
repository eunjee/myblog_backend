package jp.falsystack.falsylog_backend.request;

import lombok.Builder;
import lombok.Getter;

@Getter
public class PostCreate {

  private final String title;
  private final String content;
  private final String author;

  @Builder
  public PostCreate(String title, String content, String author) {
    this.title = title;
    this.content = content;
    this.author = author;
  }
}
