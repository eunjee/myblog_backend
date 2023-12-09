package jp.falsystack.falsylog_backend.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
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
  @Column(name = "post_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String title;
  @Lob
  private String content;

  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn
  private Member member;

  @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<PostHashTag> postHashTags = new ArrayList<>();

  @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
  private List<Comment> comments = new ArrayList<>();

  @Builder
  private Post(String title, String content, Member member) {
    this.title = title;
    this.content = content;
    this.member = member;
  }

  public void addPostHashTags(List<PostHashTag> postHashTags) {
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
}
