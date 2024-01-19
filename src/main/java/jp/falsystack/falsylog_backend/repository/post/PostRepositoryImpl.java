package jp.falsystack.falsylog_backend.repository.post;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jp.falsystack.falsylog_backend.domain.Post;
import jp.falsystack.falsylog_backend.domain.QPost;
import jp.falsystack.falsylog_backend.domain.QPostHashTag;
import jp.falsystack.falsylog_backend.request.post.PostSearch;
import lombok.RequiredArgsConstructor;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom {

    private final JPAQueryFactory query;

    @Override
    public List<Post> getList(PostSearch postSearch) {

        BooleanBuilder builder = getBuilder(postSearch);

        List<Post> posts = query.selectFrom(QPost.post)
                .leftJoin(QPost.post.postHashTags, QPostHashTag.postHashTag).fetchJoin()
                .leftJoin(QPostHashTag.postHashTag.hashTag).fetchJoin()
                .where(builder)
                .limit(postSearch.getSize())
                .offset(postSearch.getOffset())
                .orderBy(QPost.post.id.desc())
                .fetch();

        // TODO: あとでqueryDSLの勉強が終わったらリファクタリングする。
        if (StringUtils.hasText(postSearch.getHashTags())) {
            String[] tags = postSearch.getHashTags().split(",");

            return posts.stream()
                    .filter(post -> post.getPostHashTags()
                            .stream()
                            .anyMatch(postHashTag -> Arrays.asList(tags).contains(postHashTag.getHashTag().getName().substring(1))))
                    .toList();
        }
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
        return builder;
    }
}
