package jp.falsystack.falsylog_backend.repository.post;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jp.falsystack.falsylog_backend.domain.Post;
import jp.falsystack.falsylog_backend.domain.QHashTag;
import jp.falsystack.falsylog_backend.domain.QPost;
import jp.falsystack.falsylog_backend.domain.QPostHashTag;
import jp.falsystack.falsylog_backend.request.post.PostSearch;
import lombok.RequiredArgsConstructor;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.querydsl.jpa.JPAExpressions.select;

@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom {

    private final JPAQueryFactory query;

    @Override
    public List<Post> getList(PostSearch postSearch) {

        BooleanBuilder builder = getBuilder(postSearch);
        List<Post> posts = new ArrayList<>();
            posts = query.selectFrom(QPost.post)
                    .leftJoin(QPost.post.postHashTags, QPostHashTag.postHashTag).fetchJoin()
                    .leftJoin(QPostHashTag.postHashTag.hashTag).fetchJoin()
                    .where(builder)
                    .limit(postSearch.getSize())
                    .offset(postSearch.getOffset())
                    .orderBy(QPost.post.id.desc())
                    .fetch();

            
        return posts;
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
            builder.and(QPost.post.createdDateTime.goe(postSearch.getStartDate().atStartOfDay()));
        }

        if (!ObjectUtils.isEmpty(postSearch.getEndDate())) {
            builder.and(QPost.post.createdDateTime.loe(postSearch.getEndDate().plusDays(1).atStartOfDay()));
        }

        if (StringUtils.hasText(postSearch.getTitle())) {
            builder.and(QPost.post.title.contains(postSearch.getTitle()));
        }
        if(StringUtils.hasText(postSearch.getHashTags())){
            builder.and(QPost.post.id.in(
                    select(QPost.post.id)
                            .innerJoin(QPost.post.postHashTags, QPostHashTag.postHashTag)
                            .innerJoin(QPostHashTag.postHashTag.hashTag)
                            .where(QPostHashTag.postHashTag.hashTag.name.in(postSearch.getHashTags().split(",")))
            ));
        }

        return builder;
    }
}
