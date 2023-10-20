package jp.falsystack.falsylog_backend.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jp.falsystack.falsylog_backend.service.dto.PostWrite;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String title;
  private String content;
  private String author;

  @Builder
  private Post(String title, String content, String author) {
    this.title = title;
    this.content = content;
    this.author = author;
  }

  public static Post from(PostWrite postWrite) {
    return Post.builder()
        .title(postWrite.getTitle())
        .content(postWrite.getContent())
        .author(postWrite.getAuthor())
        .build();
  }
}
