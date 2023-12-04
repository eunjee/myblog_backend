package jp.falsystack.falsylog_backend.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Table(indexes = {
    @Index(name = "IDX_COMMENT_POST_ID", columnList = "post_id")
})
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String author;

  @Column(nullable = false)
  private String password;

  @Column(nullable = false)
  private String content;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "post_id")
  private Post post;

  @Builder
  public Comment(String author, String password, String content, Post post) {
    this.author = author;
    this.password = password;
    this.content = content;
    this.post = post;
  }

  public void addPost(Post post) {
    post.addComment(this);
    this.post = post;
  }
}
