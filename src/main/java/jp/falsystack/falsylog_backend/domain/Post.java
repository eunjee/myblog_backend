package jp.falsystack.falsylog_backend.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import jp.falsystack.falsylog_backend.service.dto.PostEdit;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.format.annotation.DateTimeFormat;

import static java.time.LocalDateTime.now;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post extends BaseEntity {

  @Id
  @Column(name = "post_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String title;
  @Lob
  @Column(name = "content", columnDefinition="TEXT")
  private String content;

  @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
  @JoinColumn
  private Resume resume;

  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
  @JoinColumn
  private Member member;

  @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<PostHashTag> postHashTags = new ArrayList<>();

  @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
  private List<Comment> comments = new ArrayList<>();

  @Builder
  private Post(String title, String content,LocalDateTime createdAt, Member member) {
    this.title = title;
    this.content = content;
    this.member = member;
    this.setCreatedAt(createdAt);
  }

  public void addPostHashTags(List<PostHashTag> postHashTags) {
    this.postHashTags.clear();
    this.postHashTags.addAll(postHashTags);
  }

  public void addMember(Member member) {
    member.getPosts().add(this);
    this.member = member;
  }

  public void addComment(Comment comment) {
    comments.add(comment);
  }

  public Long getMemberId() {
    return member.getId();
  }

  public void edit(PostEdit edit) {
    this.title = edit.getTitle();
    this.content = edit.getContent();
  }
}
