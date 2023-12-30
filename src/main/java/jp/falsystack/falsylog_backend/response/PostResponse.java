package jp.falsystack.falsylog_backend.response;

import java.time.LocalDateTime;
import java.util.List;
import jp.falsystack.falsylog_backend.domain.HashTag;
import jp.falsystack.falsylog_backend.domain.Post;
import lombok.Builder;
import lombok.Getter;

@Getter
public class PostResponse {

  private final Long id;
  private final String title;
  private final String content;
  private final String author;
  private final LocalDateTime createdAt;
  private final LocalDateTime updatedAt;
  private List<HashTag> hashTags;

  @Builder
  public PostResponse(Long id, String title, String content, String author, List<HashTag> hashTags,
      LocalDateTime createdAt,
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
        .hashTags(
            post.getPostHashTags().stream().map(postHashTag -> postHashTag.getHashTag()).toList())
        .author(post.getMember().getName())
        .createdAt(post.getCreatedAt())
        .updatedAt(post.getUpdatedAt())
        .build();
  }

  public void addHashTags(List<HashTag> hashTags) {
    this.hashTags = hashTags;
  }
}
