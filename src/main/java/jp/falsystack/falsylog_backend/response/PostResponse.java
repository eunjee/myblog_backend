package jp.falsystack.falsylog_backend.response;

import jp.falsystack.falsylog_backend.domain.Post;
import lombok.Builder;
import lombok.Getter;

@Getter
public class PostResponse {

  private final Long id;
  private final String title;
  private final String content;
  private final String author;

  @Builder
  private PostResponse(Long id, String title, String content, String author) {
    this.id = id;
    this.title = title;
    this.content = content;
    this.author = author;
  }

  public static PostResponse from(Post post) {
    return PostResponse.builder()
        .id(post.getId())
        .title(post.getTitle())
        .content(post.getContent())
        .author(post.getAuthor())
        .build();
  }
}
