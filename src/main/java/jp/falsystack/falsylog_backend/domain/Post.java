package jp.falsystack.falsylog_backend.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
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
  private String content;
  private String author;

  @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<PostHashTag> postHashTags = new ArrayList<>();

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

  public void addPostHashTags(String postHashTags) {
    var tags = new ArrayList<PostHashTag>();
    var pattern = Pattern.compile("#([0-9a-zA-Z가-힣ぁ-んァ-ヶー一-龯]*)");
    var matcher = pattern.matcher(postHashTags);

    while (matcher.find()) {
      var hashTag = HashTag.builder()
          .name(matcher.group())
          .build();

      var postHashTag = PostHashTag.builder()
          .post(this)
          .hashTag(hashTag)
          .build();

      tags.add(postHashTag);

    }
    this.postHashTags.addAll(tags);
  }
}
