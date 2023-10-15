package jp.falsystack.falsylog_backend.service.dto;

import jp.falsystack.falsylog_backend.request.PostCreate;
import lombok.Builder;
import lombok.Getter;

@Getter
public class PostWrite {

  private final String title;
  private final String content;
  private final String author;

  @Builder
  private PostWrite(String title, String content, String author) {
    this.title = title;
    this.content = content;
    this.author = author;
  }

  public static PostWrite from(PostCreate postCreate) {
    return PostWrite.builder()
        .title(postCreate.getTitle())
        .content(postCreate.getContent())
        .author(postCreate.getAuthor())
        .build();
  }
}
