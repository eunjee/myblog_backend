package jp.falsystack.falsylog_backend.repository.post;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jp.falsystack.falsylog_backend.domain.Post;
import jp.falsystack.falsylog_backend.domain.QPost;
import jp.falsystack.falsylog_backend.request.post.PostSearch;
import lombok.RequiredArgsConstructor;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.List;

@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom {

    private final JPAQueryFactory query;

    @Override
    public List<Post> getList(PostSearch postSearch) {

        BooleanBuilder builder = getBuilder(postSearch);

        return query.selectFrom(QPost.post)
                .where(builder)
                .limit(postSearch.getSize())
                .offset(postSearch.getOffset())
                .orderBy(QPost.post.id.desc())
                .fetch();
    }

    @Override
    public Long getCount(PostSearch postSearch) {

        BooleanBuilder builder = getBuilder(postSearch);

        return (long) query.selectFrom(QPost.post)
                .where(builder)
                .fetch().size();
    }

    private static BooleanBuilder getBuilder(PostSearch postSearch) {
        BooleanBuilder builder = new BooleanBuilder();
        if (!ObjectUtils.isEmpty(postSearch.getStartDate())) {
            builder.and(QPost.post.createdDateTime.goe(postSearch.getStartDate()));
        }

        if (!ObjectUtils.isEmpty(postSearch.getEndDate())) {
            builder.and(QPost.post.createdDateTime.loe(postSearch.getEndDate()));
        }

        if (StringUtils.hasText(postSearch.getTitle())) {
            builder.and(QPost.post.title.contains(postSearch.getTitle()));
        }
        return builder;
    }
}
