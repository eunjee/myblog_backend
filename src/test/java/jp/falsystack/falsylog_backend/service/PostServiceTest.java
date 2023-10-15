package jp.falsystack.falsylog_backend.service;

import static org.assertj.core.api.Assertions.assertThat;

import jp.falsystack.falsylog_backend.repository.PostRepository;
import jp.falsystack.falsylog_backend.request.PostCreate;
import jp.falsystack.falsylog_backend.service.dto.PostWrite;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class PostServiceTest {

  @Autowired
  private PostService postService;
  @Autowired
  private PostRepository postRepository;

  @Test
  @DisplayName("記事を作成することができる")
  void write() {
    // given
    var post = PostWrite.from(PostCreate.builder()
        .title("タイトル")
        .content("コンテンツ")
        .author("作成者")
        .build());

    // when
    postService.write(post);

    // then
    var posts = postRepository.findAll();
    assertThat(posts).hasSize(1)
        .extracting("title", "author")
        .containsExactlyInAnyOrder(
            Tuple.tuple("タイトル", "作成者")
        );
  }

}