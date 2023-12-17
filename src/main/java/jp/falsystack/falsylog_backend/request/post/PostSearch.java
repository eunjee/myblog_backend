package jp.falsystack.falsylog_backend.request.post;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostSearch {

  private static final int MAX_SIZE = 100;

  @Builder.Default
  private Integer page = 1;
  @Builder.Default
  private Integer size = 5;

  public long getOffset() {
    return (long) (Math.max(1, page) - 1) * Math.min(size, MAX_SIZE);
  }

}
