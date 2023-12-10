package jp.falsystack.falsylog_backend.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import java.util.List;
import jp.falsystack.falsylog_backend.domain.Member;
import jp.falsystack.falsylog_backend.domain.Post;
import jp.falsystack.falsylog_backend.repository.post.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
class PostRepositoryTest {

  @Autowired
  private PostRepository postRepository;

  @BeforeEach
  void beforeEach() {
    postRepository.deleteAllInBatch();
  }

  @Test
  @Transactional
  @DisplayName("ポストのIDを受取該当するポストの詳細を返す")
  void findByPostId() {
    // given
    var member = Member.builder()
        .name("テストメンバー")
        .password("1q2w3e4r")
        .email("test@test.com")
        .build();
    var post = Post.builder()
        .title("美味しいラーメンが食いたい。")
        .content("なら一蘭に行こう。ラーメンは豚骨だ。")
        .build();
    post.addMember(member);
    var savedPost = postRepository.save(post);
    var postId = savedPost.getId();

    // when
    var findPost = postRepository.findById(postId).get();

    // then
    assertThat(findPost.getTitle()).isEqualTo("美味しいラーメンが食いたい。");
    assertThat(findPost.getContent()).isEqualTo("なら一蘭に行こう。ラーメンは豚骨だ。");
    assertThat(findPost.getMember().getName()).isEqualTo("テストメンバー");
  }

  @Test
  @DisplayName("ポストを作成するとPOSTテーブルに格納される。")
  void save() {
    // given
    var member = Member.builder()
        .name("テストメンバー")
        .password("1q2w3e4r")
        .email("test@test.com")
        .build();
    var post = Post.builder()
        .title("美味しいラーメンが食いたい。")
        .content("なら一蘭に行こう。ラーメンは豚骨だ。")
        .build();
    post.addMember(member);

    // when
    postRepository.save(post);

    // then
    var posts = postRepository.findAll();
    assertThat(posts)
        .hasSize(1)
        .extracting("title", "content")
        .containsExactlyInAnyOrder(
            tuple("美味しいラーメンが食いたい。", "なら一蘭に行こう。ラーメンは豚骨だ。")
        );
  }
}