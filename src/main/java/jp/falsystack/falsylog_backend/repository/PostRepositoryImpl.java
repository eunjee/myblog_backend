package jp.falsystack.falsylog_backend.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import jp.falsystack.falsylog_backend.domain.Post;
import jp.falsystack.falsylog_backend.domain.QPost;
import jp.falsystack.falsylog_backend.request.PostSearch;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom{

  private final JPAQueryFactory query;

  @Override
  public List<Post> getList(PostSearch postSearch) {
    return query.selectFrom(QPost.post)
        .limit(postSearch.getSize())
        .offset(postSearch.getOffset())
        .orderBy(QPost.post.id.desc())
        .fetch();
  }
}
