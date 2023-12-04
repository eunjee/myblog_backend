package jp.falsystack.falsylog_backend.response;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import jp.falsystack.falsylog_backend.domain.HashTag;
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
  private final List<HashTag> hashTags;

  @Builder
  public TagPostResponse(Long id, String title, String content, String author,
      LocalDateTime createdAt, LocalDateTime updatedAt, List<HashTag> hashTags) {
    this.id = id;
    this.title = title;
    this.content = content;
    this.author = author;
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
    this.hashTags = hashTags;
  }

  public static TagPostResponse from(Post post) {
    var tags = new ArrayList<HashTag>();
    post.getPostHashTags().forEach(postHashTag -> {
      tags.add(postHashTag.getHashTag());
    });

    return TagPostResponse.builder()
        .id(post.getId())
        .title(post.getTitle())
        .content(post.getContent())
        .author(post.getMember().getName())
        .hashTags(tags)
        .createdAt(post.getCreatedAt())
        .updatedAt(post.getUpdatedAt())
        .build();
  }
}
