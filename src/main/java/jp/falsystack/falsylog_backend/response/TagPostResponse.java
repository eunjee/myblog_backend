package jp.falsystack.falsylog_backend.response;

import java.time.LocalDateTime;
import jp.falsystack.falsylog_backend.domain.Post;
import lombok.Builder;
import lombok.Getter;

@Getter
public class TagPostResponse {

  private final Long id;
  private final String title;
  private final String content;
  private final String author;
  private final LocalDateTime createdAt;
  private final LocalDateTime updatedAt;

  @Builder
  public TagPostResponse(Long id, String title, String content, String author,
      LocalDateTime createdAt, LocalDateTime updatedAt) {
    this.id = id;
    this.title = title;
    this.content = content;
    this.author = author;
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
  }

  public static TagPostResponse from(Post post) {
    return TagPostResponse.builder()
        .id(post.getId())
        .title(post.getTitle())
        .content(post.getContent())
        .author(post.getAuthor())
        .createdAt(post.getCreatedAt())
        .updatedAt(post.getUpdatedAt())
        .build();
  }
}
