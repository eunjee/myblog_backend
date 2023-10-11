package jp.falsystack.falsylog_backend.request;

import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;

@Getter
public class PostCreate {

  private final String title;
  private final String content;
  private final String author;
  private final LocalDate createdAt;

  @Builder
  public PostCreate(String title, String content, String author, LocalDate createdAt) {
    this.title = title;
    this.content = content;
    this.author = author;
    this.createdAt = createdAt;
  }
}
