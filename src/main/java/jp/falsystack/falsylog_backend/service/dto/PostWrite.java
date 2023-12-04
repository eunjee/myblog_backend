package jp.falsystack.falsylog_backend.service.dto;

import jp.falsystack.falsylog_backend.request.post.PostCreate;
import lombok.Builder;
import lombok.Getter;

@Getter
public class PostWrite {

  private final String title;
  private final String content;
  private final Long memberId;
  private final String hashTags;

  @Builder
  private PostWrite(String title, String content, Long memberId, String hashTags) {
    this.title = title;
    this.content = content;
    this.memberId = memberId;
    this.hashTags = hashTags;
  }

  public static PostWrite of(PostCreate postCreate, Long memberId) {
    return PostWrite.builder()
        .title(postCreate.getTitle())
        .content(postCreate.getContent())
        .memberId(memberId)
        .hashTags(postCreate.getHashTags())
        .build();
  }
}
