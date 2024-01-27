package jp.falsystack.falsylog_backend.repository.post;

import jp.falsystack.falsylog_backend.domain.Post;
import jp.falsystack.falsylog_backend.request.post.PostSearch;

import java.util.List;

public interface PostRepositoryCustom {

  List<Post> getList(PostSearch postSearch);
  List<Post> getMemberPostList(Long memberId, PostSearch postSearch);

  Long getCount(PostSearch postSearch);
}
