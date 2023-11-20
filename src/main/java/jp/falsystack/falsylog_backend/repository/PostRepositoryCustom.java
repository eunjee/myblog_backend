package jp.falsystack.falsylog_backend.repository;

import java.util.List;
import jp.falsystack.falsylog_backend.domain.Post;
import jp.falsystack.falsylog_backend.request.PostSearch;

public interface PostRepositoryCustom {

  List<Post> getList(PostSearch postSearch);
}
