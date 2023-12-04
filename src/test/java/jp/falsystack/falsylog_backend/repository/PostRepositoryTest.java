package jp.falsystack.falsylog_backend.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import java.util.List;
import jp.falsystack.falsylog_backend.domain.Post;
import jp.falsystack.falsylog_backend.repository.post.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class PostRepositoryTest {

  @Autowired
  private PostRepository postRepository;

  @BeforeEach
  void beforeEach() {
    postRepository.deleteAllInBatch();
  }

  private static Post createPostDto(int position) {
    return Post.builder()
        .title("タイトル" + position)
        .content("コンテンツ" + position)
        .author("作成者" + position)
        .build();
  }

  @Test
  @DisplayName("記事のIDを受取該当する記事を返す")
  void findByPostId() {
    // given
    var post = createPostDto(1);
    var savedPost = postRepository.save(post);
    var postId = savedPost.getId();

    // when
    var findPost = postRepository.findById(postId).get();

    // then
    assertThat(findPost.getTitle()).isEqualTo("タイトル1");
    assertThat(findPost.getContent()).isEqualTo("コンテンツ1");
    assertThat(findPost.getAuthor()).isEqualTo("作成者1");
  }

  @Test
  @DisplayName("記事を作成するとPostテーブルに格納される。")
  void save() {
    // given
    var post1 = createPostDto(1);
    var post2 = createPostDto(2);
    var post3 = createPostDto(3);

    // when
    postRepository.saveAll(List.of(post1, post2, post3));

    // then
    var posts = postRepository.findAll();
    assertThat(posts)
        .hasSize(3)
        .extracting("title", "content", "author")
        .containsExactlyInAnyOrder(
            tuple("タイトル1", "コンテンツ1", "作成者1"),
            tuple("タイトル2", "コンテンツ2", "作成者2"),
            tuple("タイトル3", "コンテンツ3", "作成者3")
        );
  }
}