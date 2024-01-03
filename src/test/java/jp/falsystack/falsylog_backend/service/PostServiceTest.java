package jp.falsystack.falsylog_backend.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.tuple;

import java.util.List;
import java.util.Optional;
import jp.falsystack.falsylog_backend.annotation.CustomWithMockUser;
import jp.falsystack.falsylog_backend.domain.HashTag;
import jp.falsystack.falsylog_backend.domain.Member;
import jp.falsystack.falsylog_backend.domain.Post;
import jp.falsystack.falsylog_backend.domain.PostHashTag;
import jp.falsystack.falsylog_backend.exception.PostNotFound;
import jp.falsystack.falsylog_backend.repository.HashTagRepository;
import jp.falsystack.falsylog_backend.repository.MemberRepository;
import jp.falsystack.falsylog_backend.repository.PostHashTagRepository;
import jp.falsystack.falsylog_backend.repository.post.PostRepository;
import jp.falsystack.falsylog_backend.request.post.PostCreate;
import jp.falsystack.falsylog_backend.request.post.PostSearch;
import jp.falsystack.falsylog_backend.response.PostResponse;
import jp.falsystack.falsylog_backend.service.dto.PostEdit;
import jp.falsystack.falsylog_backend.service.dto.PostWrite;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

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
  @Autowired
  private PostHashTagRepository postHashTagRepository;

  @AfterEach
  void afterEach() {
    postHashTagRepository.deleteAllInBatch();
    hashTagRepository.deleteAllInBatch();
    postRepository.deleteAllInBatch();
    memberRepository.deleteAllInBatch();
  }

  @CustomWithMockUser
  @Test
  @DisplayName("ハッシュタグを含んでポストを作成することができる")
  void writeWithTags() {
    // given
    var principalMember = memberRepository.findAll().get(0);
    var postWrite = PostWrite.of(PostCreate.builder()
        .title("美味しいラーメンが食いたい。")
        .content("なら一蘭に行こう。ラーメンは豚骨だ。")
        .hashTags("#Spring#一蘭")
        .build(), principalMember.getId());

    // when
    postService.write(postWrite);

    // then
    var posts = postRepository.findAll();
    var tags = hashTagRepository.findAll();

    assertThat(posts).hasSize(1)
        .extracting("title", "content")
        .containsExactlyInAnyOrder(
            tuple("美味しいラーメンが食いたい。", "なら一蘭に行こう。ラーメンは豚骨だ。")
        );

    assertThat(tags).hasSize(2)
        .extracting("name")
        .contains("#Spring", "#一蘭");
  }

  @CustomWithMockUser
  @Test
  @DisplayName("ハッシュタグを含んで無くても問題なくポストを作成することができる")
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
  @DisplayName("受け取ったポストのIDに該当するポストの詳細を返却する")
  void getPost() {
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
    var postResponse = postService.getPost(postId);

    // then
    assertThat(postResponse.getTitle()).isEqualTo("美味しいラーメンが食いたい。");
    assertThat(postResponse.getContent()).isEqualTo("なら一蘭に行こう。ラーメンは豚骨だ。");
    assertThat(postResponse.getAuthor()).isEqualTo("テストメンバー");
  }

  @Test
  @DisplayName("ポストのIDを受取該当するポストがない場合例外を返す")
  void getPostFailed() {
    // given
    var postId = 1L;

    // expected
    assertThatThrownBy(() -> postService.getPost(postId))
        .isInstanceOf(PostNotFound.class)
        .hasMessage("찾으시는 게시글이 없습니다."); // お探しの記事がないです。
  }

  @Test
  @DisplayName("ポストのリストを返す")
  void getPosts() {
    // given
    var member = Member.builder()
        .name("テストメンバー")
        .password("1q2w3e4r")
        .email("test@test.com")
        .build();
    var post1 = Post.builder()
        .title("美味しいラーメンが食いたい。")
        .content("なら一蘭に行こう。ラーメンは豚骨だ。")
        .build();
    post1.addMember(member);

    var post2 = Post.builder()
        .title("美味しいラーメンが食いたい。")
        .content("なら一蘭に行こう。ラーメンは豚骨だ。")
        .build();
    post2.addMember(member);

    var post3 = Post.builder()
        .title("美味しいラーメンが食いたい。")
        .content("なら一蘭に行こう。ラーメンは豚骨だ。")
        .build();
    post3.addMember(member);
    postRepository.saveAll(List.of(post1, post2, post3));

    // when
    var posts = postService.getPosts(new PostSearch());

    // then
    assertThat(posts.getIsLast()).isTrue();
    assertThat(posts.getTotalLength()).isEqualTo(3);
    assertThat(posts.getLastPage()).isEqualTo(1);

    assertThat(posts.getPostResponses()).hasSize(3).extracting("title", "content")
        .containsExactlyInAnyOrder(
            tuple("美味しいラーメンが食いたい。", "なら一蘭に行こう。ラーメンは豚骨だ。"),
            tuple("美味しいラーメンが食いたい。", "なら一蘭に行こう。ラーメンは豚骨だ。"),
            tuple("美味しいラーメンが食いたい。", "なら一蘭に行こう。ラーメンは豚骨だ。")
        );
  }

  @Test
  @DisplayName("指定IDのポストを削除する")
  void deletePost() {
    // given
    var member = Member.builder()
        .name("テストメンバー")
        .password("1q2w3e4r")
        .email("test@test.com")
        .build();
    var post1 = Post.builder()
        .title("美味しいラーメンが食いたい。")
        .content("なら一蘭に行こう。ラーメンは豚骨だ。")
        .build();
    post1.addMember(member);

    var post2 = Post.builder()
        .title("美味しいラーメンが食いたい。")
        .content("なら一蘭に行こう。ラーメンは豚骨だ。")
        .build();
    post2.addMember(member);

    var post3 = Post.builder()
        .title("美味しいラーメンが食いたい。")
        .content("なら一蘭に行こう。ラーメンは豚骨だ。")
        .build();
    post3.addMember(member);
    postRepository.saveAll(List.of(post1, post2, post3));

    // when
    postService.delete(post1.getId());

    // then
    assertThat(postRepository.findAll().size()).isEqualTo(2);
    assertThatThrownBy(() -> postService.getPost(post1.getId())).isInstanceOf(PostNotFound.class);
  }

  @Test
  @Transactional
  @DisplayName("ポストのIDと受け取った値を元に更新を行う")
  void updatePost() {
    // given
    var member = Member.builder()
        .name("テストメンバー")
        .password("1q2w3e4r")
        .email("test@test.com")
        .build();
    var post1 = Post.builder()
        .title("美味しいラーメンが食いたい。")
        .content("なら一蘭に行こう。ラーメンは豚骨だ。")
        .build();
    var hashTag = HashTag.builder()
        .name("#ラーメン")
        .build();
    var postHashTag = PostHashTag.builder()
        .build();

    post1.addMember(member);
    postHashTag.addPost(post1);
    postHashTag.addHashTag(hashTag);
    postRepository.save(post1);

    var postEdit = PostEdit.builder()
        .title("美味しい焼肉が食いたい。")
        .content("美味しい焼肉店をGoogle Mapから探しましょう")
        .hashTags("#焼肉#カルビ#Java")
        .build();

    // when
    postService.update(post1.getId(), postEdit);

    // then
    var findPost = postRepository.findById(post1.getId()).get();
    assertThat(findPost.getTitle()).isEqualTo("美味しい焼肉が食いたい。");
    assertThat(findPost.getContent()).isEqualTo("美味しい焼肉店をGoogle Mapから探しましょう");
    var names = findPost.getPostHashTags().stream()
        .map(p -> p.getHashTag().getName());
    assertThat(names).hasSize(3).containsExactly("#焼肉", "#カルビ", "#Java");
  }
}