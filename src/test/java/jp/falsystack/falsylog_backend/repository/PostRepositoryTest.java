package jp.falsystack.falsylog_backend.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import java.util.List;
import jp.falsystack.falsylog_backend.domain.Post;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class PostRepositoryTest {

  @Autowired
  private PostRepository postRepository;

  @Test
  @DisplayName("記事を作成するとPostテーブルに格納される。")
  void save() {
    // given
    var post1 = Post.builder()
        .title("タイトル１")
        .content("コンテンツ１")
        .author("作成者１")
        .build();
    var post2 = Post.builder()
        .title("タイトル２")
        .content("コンテンツ２")
        .author("作成者２")
        .build();
    var post3 = Post.builder()
        .title("タイトル３")
        .content("コンテンツ３")
        .author("作成者３")
        .build();

    // when
    postRepository.saveAll(List.of(post1, post2, post3));

    // then
    var posts = postRepository.findAll();
    assertThat(posts)
        .hasSize(3)
        .extracting("title", "content", "author")
        .containsExactlyInAnyOrder(
            tuple("タイトル１", "コンテンツ１", "作成者１"),
            tuple("タイトル２", "コンテンツ２", "作成者２"),
            tuple("タイトル３", "コンテンツ３", "作成者３")
        );
  }
}