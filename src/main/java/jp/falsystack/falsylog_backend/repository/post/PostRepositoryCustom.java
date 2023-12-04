package jp.falsystack.falsylog_backend.repository.post;

import java.util.List;
import jp.falsystack.falsylog_backend.domain.Post;
import jp.falsystack.falsylog_backend.request.post.PostSearch;

public interface PostRepositoryCustom {

  List<Post> getList(PostSearch postSearch);
}
