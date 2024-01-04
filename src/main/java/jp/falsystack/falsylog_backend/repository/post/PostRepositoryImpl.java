package jp.falsystack.falsylog_backend.repository.post;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jp.falsystack.falsylog_backend.domain.Post;
import jp.falsystack.falsylog_backend.domain.QPost;
import jp.falsystack.falsylog_backend.request.post.PostSearch;
import lombok.RequiredArgsConstructor;
import org.springframework.util.ObjectUtils;

import java.util.List;

@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom {

    private final JPAQueryFactory query;

    @Override
    public List<Post> getList(PostSearch postSearch) {

        BooleanBuilder builder = new BooleanBuilder();
        if (!ObjectUtils.isEmpty(postSearch.getStartDate())) {
            builder.and(QPost.post.createdDateTime.goe(postSearch.getStartDate()));
        }

        if (!ObjectUtils.isEmpty(postSearch.getEndDate())) {
            builder.and(QPost.post.createdDateTime.loe(postSearch.getEndDate()));
        }

        return query.selectFrom(QPost.post)
                .where(builder)
                .limit(postSearch.getSize())
                .offset(postSearch.getOffset())
                .orderBy(QPost.post.id.desc())
                .fetch();
    }

    @Override
    public Long getCount(PostSearch postSearch) {

        BooleanBuilder builder = new BooleanBuilder();
        if (!ObjectUtils.isEmpty(postSearch.getStartDate())) {
            builder.and(QPost.post.createdDateTime.goe(postSearch.getStartDate()));
        }

        if (!ObjectUtils.isEmpty(postSearch.getEndDate())) {
            builder.and(QPost.post.createdDateTime.loe(postSearch.getEndDate()));
        }

        return (long) query.selectFrom(QPost.post)
                .where(builder)
                .fetch().size();
    }
}
