package jp.falsystack.falsylog_backend.response;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import jp.falsystack.falsylog_backend.domain.HashTag;
import jp.falsystack.falsylog_backend.domain.Post;
import jp.falsystack.falsylog_backend.domain.PostHashTag;
import lombok.Builder;
import lombok.Getter;

@Getter
public class PostResponse {

  private final Long id;
  private final String title;
  private final String content;
  private final String author;
  private List<HashTag> hashTags;
  private final LocalDateTime createdAt;
  private final LocalDateTime updatedAt;

  @Builder
  public PostResponse(Long id, String title, String content, String author, List<HashTag> hashTags, LocalDateTime createdAt,
      LocalDateTime updatedAt) {
    this.id = id;
    this.title = title;
    this.content = content;
    this.author = author;
    this.hashTags = hashTags;
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
  }

  public static PostResponse from(Post post) {
    return PostResponse.builder()
        .id(post.getId())
        .title(post.getTitle())
        .content(post.getContent())
        .author(post.getAuthor())
        .createdAt(post.getCreatedAt())
        .updatedAt(post.getUpdatedAt())
        .build();
  }

  public void addHashTags(List<HashTag> hashTags) {
    this.hashTags = hashTags;
  }
}
