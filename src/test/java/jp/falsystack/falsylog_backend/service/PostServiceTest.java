package jp.falsystack.falsylog_backend.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import java.util.List;
import jp.falsystack.falsylog_backend.domain.Post;
import jp.falsystack.falsylog_backend.repository.PostRepository;
import jp.falsystack.falsylog_backend.request.PostCreate;
import jp.falsystack.falsylog_backend.response.PostResponse;
import jp.falsystack.falsylog_backend.service.dto.PostWrite;
import org.junit.jupiter.api.BeforeEach;
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

  @BeforeEach
  void beforeEach() {
    postRepository.deleteAllInBatch();
  }

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
            tuple("タイトル", "作成者")
        );
  }

  @Test
  @DisplayName("DBから読んできたPostをPostResponseに書き換えてリストを返す")
  void getPosts() {
    // given
    var postDto1 = createPostDto(1);
    var postDto2 = createPostDto(2);
    var postDto3 = createPostDto(3);
    postRepository.saveAll(List.of(postDto1, postDto2, postDto3));

    // when
    var posts = postService.getPosts();

    // then
    assertThat(posts.get(0)).isInstanceOf(PostResponse.class);
    assertThat(posts).hasSize(3).extracting("title", "content")
        .containsExactlyInAnyOrder(
            tuple("記事タイトル1", "コンテンツ1"),
            tuple("記事タイトル2", "コンテンツ2"),
            tuple("記事タイトル3", "コンテンツ3")
        );
  }

  private static Post createPostDto(int count) {
    return Post.builder()
        .title("記事タイトル" + count)
        .content("コンテンツ" + count)
        .author("falsystack" + count)
        .build();
  }
}