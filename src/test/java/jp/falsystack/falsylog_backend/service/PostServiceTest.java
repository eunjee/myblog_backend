package jp.falsystack.falsylog_backend.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.tuple;

import java.util.List;
import jp.falsystack.falsylog_backend.annotation.CustomWithMockUser;
import jp.falsystack.falsylog_backend.domain.Member;
import jp.falsystack.falsylog_backend.domain.Post;
import jp.falsystack.falsylog_backend.exception.PostNotFound;
import jp.falsystack.falsylog_backend.repository.HashTagRepository;
import jp.falsystack.falsylog_backend.repository.MemberRepository;
import jp.falsystack.falsylog_backend.repository.post.PostRepository;
import jp.falsystack.falsylog_backend.request.post.PostCreate;
import jp.falsystack.falsylog_backend.request.post.PostSearch;
import jp.falsystack.falsylog_backend.response.PostResponse;
import jp.falsystack.falsylog_backend.service.dto.PostWrite;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
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
  @Autowired
  private HashTagRepository hashTagRepository;
  @Autowired
  private MemberRepository memberRepository;

  private static Post createPostDto(int count) {
    return Post.builder()
        .title("記事タイトル" + count)
        .content("コンテンツ" + count)
        // TODO: 直す
//        .member()
        .build();
  }

  @BeforeEach
  void beforeEach() {
    postRepository.deleteAll();
//    postRepository.deleteAllInBatch();
  }

  @Disabled
  @Test
  @DisplayName("ハッシュタグを選んで記事を作成することができる")
  void writeWithTags() {
    // given
    var post = PostWrite.of(PostCreate.builder()
        .title("タイトル")
        .content("コンテンツ")
        .hashTags("#Spring#Java")
        .build(), 1L);

    // when
    postService.write(post);

    // then
    var posts = postRepository.findAll();
    var tags = hashTagRepository.findAll();

    assertThat(posts).hasSize(1)
        .extracting("title", "author")
        .containsExactlyInAnyOrder(
            tuple("タイトル", "作成者")
        );

    assertThat(tags).hasSize(2)
        .extracting("name")
        .contains("#Spring", "#Java");
  }

  @CustomWithMockUser
  @Test
  @DisplayName("hashTag無しでポストを問題なく作成することができる")
  void write() {
    // given
    var member = memberRepository.findAll().get(0);
    var postWrite = PostWrite.builder()
        .title("美味しいラーメンが食いたい。")
        .content("なら一蘭に行こう。ラーメンは豚骨だ。")
        .memberId(member.getId())
        .build();

    // when
    postService.write(postWrite);

    // then
    var posts = postRepository.findAll();

    assertThat(posts).hasSize(1)
        .extracting("title", "content")
        .contains(
            tuple("美味しいラーメンが食いたい。", "なら一蘭に行こう。ラーメンは豚骨だ。")
        );
  }

  @Test
  @DisplayName("記事のIDを受取該当する記事の詳細を返す")
  void getPost() {
    // given
    var post = createPostDto(0);
    var savedPost = postRepository.save(post);
    var postId = savedPost.getId();

    // when
    var postResponse = postService.getPost(postId);

    // then
    assertThat(postResponse.getTitle()).isEqualTo("記事タイトル0");
    assertThat(postResponse.getContent()).isEqualTo("コンテンツ0");
    assertThat(postResponse.getAuthor()).isEqualTo("falsystack0");
  }

  @Test
  @DisplayName("記事のIDを受取該当する記事がない場合例外を返す")
  void getPostFailed() {
    // given
    var postId = 1L;

    // expected
    assertThatThrownBy(() -> postService.getPost(postId))
        .isInstanceOf(PostNotFound.class)
        .hasMessage("찾으시는 게시글이 없습니다."); // お探しの記事がないです。
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
    var posts = postService.getPosts(new PostSearch());

    // then
    assertThat(posts.get(0)).isInstanceOf(PostResponse.class);
    assertThat(posts).hasSize(3).extracting("title", "content")
        .containsExactlyInAnyOrder(
            tuple("記事タイトル1", "コンテンツ1"),
            tuple("記事タイトル2", "コンテンツ2"),
            tuple("記事タイトル3", "コンテンツ3")
        );
  }
}