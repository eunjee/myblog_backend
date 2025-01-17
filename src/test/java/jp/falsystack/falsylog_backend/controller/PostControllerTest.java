package jp.falsystack.falsylog_backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jp.falsystack.falsylog_backend.annotation.CustomWithMockUser;
import jp.falsystack.falsylog_backend.domain.HashTag;
import jp.falsystack.falsylog_backend.domain.Member;
import jp.falsystack.falsylog_backend.domain.Post;
import jp.falsystack.falsylog_backend.domain.PostHashTag;
import jp.falsystack.falsylog_backend.repository.HashTagRepository;
import jp.falsystack.falsylog_backend.repository.MemberRepository;
import jp.falsystack.falsylog_backend.repository.PostHashTagRepository;
import jp.falsystack.falsylog_backend.repository.post.PostRepository;
import jp.falsystack.falsylog_backend.request.post.PostCreate;
import jp.falsystack.falsylog_backend.request.post.PostUpdate;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@ExtendWith(RestDocumentationExtension.class)
public class PostControllerTest {

  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private ObjectMapper objectMapper;
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

  /**
   * TODO: このコメントがいるかもっと考えてみる
   * Post登録テスト
   */


  @CustomWithMockUser
  @Test
  @DisplayName("/post hashTag無しでポスト登録を要請すると問題なく登録される")
  void post() throws Exception {
    // given
    var request = PostCreate.builder()
        .title("美味しいラーメンが食いたい。")
        .content("なら一蘭に行こう。ラーメンは豚骨だ。")
        .build();
    String json = objectMapper.writeValueAsString(request);

    // expected
    mockMvc.perform(MockMvcRequestBuilders.post("/post")
            .contentType(APPLICATION_JSON)
            .content(json))
        .andExpect(status().isOk())
        .andDo(print());
  }


  @CustomWithMockUser
  @Test
  @DisplayName("/post ポストを登録する時にhashtagがあればhashtagも一緒に登録される。")
  void postCreateWithHashTags() throws Exception {
    // given
    var request = PostCreate.builder()
        .title("美味しいラーメンが食いたい。")
        .content("なら一蘭に行こう。ラーメンは豚骨だ。")
        .hashTags("#一蘭#ラーメン#日本料理")
        .build();
    var json = objectMapper.writeValueAsString(request);

    // expected
    mockMvc.perform(MockMvcRequestBuilders.post("/post")
            .contentType(APPLICATION_JSON)
            .content(json)
        ).andExpect(status().isOk())
        .andDo(print());

    var hashTags = hashTagRepository.findAll();
    assertThat(hashTags)
        .hasSize(3)
        .extracting("name")
        .containsExactlyInAnyOrder(
            "#一蘭", "#ラーメン", "#日本料理"
        );
  }


  @Test
  @DisplayName("/post タイトルが「null」とポスト登録に失敗する。")
  void postFail_No_Title() throws Exception {
    // given
    var request = PostCreate.builder()
        .title(null)
        .content("なら一蘭に行こう。ラーメンは豚骨だ。")
        .hashTags(null)
        .build();
    String json = objectMapper.writeValueAsString(request);

    // expected
    mockMvc.perform(MockMvcRequestBuilders.post("/post")
            .contentType(APPLICATION_JSON)
            .content(json))
        .andExpect(status().isBadRequest())
        .andExpectAll(
            jsonPath("$.message", is("잘못된 요청입니다")),
            jsonPath("$.validation.title", is("제목을 입력해주세요"))
        )
        .andDo(print());
  }


  @Test
  @DisplayName("/post タイトルが「' '」とポスト登録に失敗する。")
  void postFail_Not_Empty() throws Exception {
    // given
    var request = PostCreate.builder()
        .title(" ")
        .content("なら一蘭に行こう。ラーメンは豚骨だ。")
        .hashTags(null)
        .build();
    String json = objectMapper.writeValueAsString(request);

    // expected
    mockMvc.perform(MockMvcRequestBuilders.post("/post")
            .contentType(APPLICATION_JSON)
            .content(json))
        .andExpect(status().isBadRequest())
        .andExpectAll(
            jsonPath("$.message", is("잘못된 요청입니다")),
            jsonPath("$.validation.title", is("제목을 입력해주세요"))
        )
        .andDo(print());
  }


  @CustomWithMockUser
  @Test
  @DisplayName("/post タイトルは1~30文字で入力しないとポスト登録に失敗する。")
  void postFail_Least_One_Word() throws Exception {
    // given
    var request = PostCreate.builder()
        .title("美味しいラーメンが食いたいけど一蘭まで歩くのはキツイ.......................")
        .content("ならウーバーイーツがあるぞ")
        .hashTags(null)
        .build();
    String json = objectMapper.writeValueAsString(request);

    // expected
    mockMvc.perform(MockMvcRequestBuilders.post("/post")
            .contentType(APPLICATION_JSON)
            .content(json))
        .andExpect(status().isBadRequest())
        .andExpectAll(
            jsonPath("$.message", is("잘못된 요청입니다")),
            jsonPath("$.validation.title", is("제목은 1~30 글자로 작성해 주세요"))
        )
        .andDo(print());
  }


  @Test
  @DisplayName("/post Contentがないとポスト登録に失敗する。")
  void postFailContentMustBeNotBlank() throws Exception {
    // given목
    var request = PostCreate.builder()
        .title("美味しいラーメンが食いたい。")
        .content(null)
        .hashTags(null)
        .build();
    String json = objectMapper.writeValueAsString(request);

    // expected
    mockMvc.perform(MockMvcRequestBuilders.post("/post")
            .contentType(APPLICATION_JSON)
            .content(json))
        .andExpect(status().isBadRequest())
        .andExpectAll(
            jsonPath("$.message", is("잘못된 요청입니다")),
            jsonPath("$.validation.content", is("내용을 입력해주세요"))
        )
        .andDo(print());
  }


  @Test
  @DisplayName("/post Content は10文字以上入力しないとポスト登録に失敗する。")
  void postFailContentMustBeLeastTenWord() throws Exception {
    // given
    var request = PostCreate.builder()
        .title("美味しいラーメンが食いたい。")
        .content("なら一蘭に行こう。")
        .hashTags(null)
        .build();
    String json = objectMapper.writeValueAsString(request);

    // expected
    mockMvc.perform(MockMvcRequestBuilders.post("/post")
            .contentType(APPLICATION_JSON)
            .content(json))
        .andExpect(status().isBadRequest())
        .andExpectAll(
            jsonPath("$.message", is("잘못된 요청입니다")),
            jsonPath("$.validation.content", is("10글자 이상 입력해주세요"))
        )
        .andDo(print());
  }

  /**
   * Post照会
   */


  @Test
  @DisplayName("/post/{postId} ポストのIDで照会するとポストの詳細が返ってくる。")
  void getPost() throws Exception {
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

    // expected
    mockMvc.perform(get("/post/{postId}", savedPost.getId())
            .contentType(APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id", is(savedPost.getId().intValue())))
        .andExpect(jsonPath("$.title", is("美味しいラーメンが食いたい。")))
        .andExpect(jsonPath("$.content", is("なら一蘭に行こう。ラーメンは豚骨だ。")))
        .andExpect(jsonPath("$.author", is("テストメンバー")))
        .andDo(print());
  }


  @Test
  @DisplayName("/post/{postId} ポストのIDで照会するとhashTagが含まれたポストの詳細が返ってくる。")
  void getPostWithHashTags() throws Exception {
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
    var hashTag = HashTag.builder()
        .name("#ラーメン")
        .build();
    var postHashTag = PostHashTag.builder()
        .build();
    postHashTag.addPost(post);
    postHashTag.addHashTag(hashTag);
    post.addMember(member);
    var savedPost = postRepository.save(post);

    // expected
    mockMvc.perform(get("/post/{postId}", savedPost.getId())
            .contentType(APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id", is(savedPost.getId().intValue())))
        .andExpect(jsonPath("$.title", is("美味しいラーメンが食いたい。")))
        .andExpect(jsonPath("$.content", is("なら一蘭に行こう。ラーメンは豚骨だ。")))
        .andExpect(jsonPath("$.author", is("テストメンバー")))
        .andExpect(jsonPath("$.hashTags.[0].name", is("#ラーメン")))
        .andDo(print());
  }

  /**
   * Post削除
   */

  @CustomWithMockUser
  @Transactional
  @Test
  @DisplayName("/post/{postId} ポストのIDを元に削除を行う")
  void deletePost() throws Exception {
    // given
    // 先に保存されたMockUserのデータを呼び出す
    var member = memberRepository.findAll().get(0);
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
    assertThat(postRepository.findAll().size()).isEqualTo(3);

    // expected
    mockMvc.perform(MockMvcRequestBuilders.delete("/post/{potId}", post1.getId())
            .contentType(APPLICATION_JSON))
        .andExpect(status().isOk())
        .andDo(print());

    assertThat(postRepository.findAll().size()).isEqualTo(2);
  }

  @CustomWithMockUser
  @Transactional
  @Test
  @DisplayName("/post/{postId} 文字のポストのIDでポストの削除をリクエストすると失敗する")
  void deletePostFail() throws Exception {
    // given
    // 先に保存されたMockUserのデータを呼び出す
    var member = memberRepository.findAll().get(0);
    var post = Post.builder()
        .title("美味しいラーメンが食いたい。")
        .content("なら一蘭に行こう。ラーメンは豚骨だ。")
        .build();
    post.addMember(member);
    var savedPost = postRepository.save(post);

    // expected
    mockMvc.perform(MockMvcRequestBuilders.delete("/post/{potId}", "aaa")
            .contentType(APPLICATION_JSON))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message", is("잘못된 요청입니다")))
        .andDo(print());
  }

  @Test
  @DisplayName("/posts ポスト一覧を返す")
  void getPosts() throws Exception {
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

    // expected
    mockMvc.perform(get("/posts")
            .contentType(APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.isLast", is(true)))
        .andExpect(jsonPath("$.totalLength", is(3)))
        .andExpect(jsonPath("$.lastPage", is(1)))
        .andExpectAll(
            jsonPath("$.postResponses.[0].title", is("美味しいラーメンが食いたい。")),
            jsonPath("$.postResponses.[0].content", is("なら一蘭に行こう。ラーメンは豚骨だ。")),
            jsonPath("$.postResponses.[0].author", is("テストメンバー"))
        )
        .andDo(print());
  }

  @Test
  @DisplayName("/posts 期間内のポスト一覧を返す")
  void getPostsWithDuration() throws Exception {
    // given
    var member = Member.builder()
            .name("テストメンバー")
            .password("1q2w3e4r")
            .email("test@test.com")
            .build();
    var post1 = Post.builder()
            .title("美味しいラーメンが食いたい。")
            .content("なら一蘭に行こう。ラーメンは豚骨だ。")
            .createdAt(LocalDateTime.of(2024,1,1,10,10))
            .build();
    post1.addMember(member);

    var post2 = Post.builder()
            .title("美味しいラーメンが食いたい。")
            .content("なら一蘭に行こう。ラーメンは豚骨だ。")
            .createdAt(LocalDateTime.of(2024,1,1,10,10))
            .build();
    post2.addMember(member);

    var post3 = Post.builder()
            .title("美味しいラーメンが食いたい。")
            .content("なら一蘭に行こう。ラーメンは豚骨だ。")
            .createdAt(LocalDateTime.of(2024,1,5,10,10))
            .build();
    post3.addMember(member);
    postRepository.saveAll(List.of(post1, post2, post3));

//    List<Post> all = postRepository.findAll();
//    for(Post post:all){
//      System.out.println("post.getCreatedDateTime() = " + post.getCreatedDateTime());
//      System.out.println("post.getCreatedAt() = " + post.getCreatedAt());
//    }

    // expected
    mockMvc.perform(get("/posts?startDate=2024-01-01&endDate=2024-01-04")
                    .contentType(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.isLast", is(true)))
            .andExpect(jsonPath("$.totalLength", is(2)))
            .andExpect(jsonPath("$.lastPage", is(1)))
            .andExpectAll(
                    jsonPath("$.postResponses.[0].title", is("美味しいラーメンが食いたい。")),
                    jsonPath("$.postResponses.[0].content", is("なら一蘭に行こう。ラーメンは豚骨だ。")),
                    jsonPath("$.postResponses.[0].author", is("テストメンバー"))
            )
            .andDo(print());
  }

  @Test
  @DisplayName("/posts 指定タイトルのポスト一覧を返す")
  void getPostsWithTitle() throws Exception {
    // given
    var member = Member.builder()
            .name("テストメンバー")
            .password("1q2w3e4r")
            .email("test@test.com")
            .build();
    var post1 = Post.builder()
            .title("豚骨ラーメン")
            .content("なら一蘭に行こう。ラーメンは豚骨だ。")
            .createdAt(LocalDateTime.of(2024,1,1,10,10))
            .build();
    post1.addMember(member);

    var post2 = Post.builder()
            .title("明太子パスタ")
            .content("なら一蘭に行こう。ラーメンは豚骨だ。")
            .createdAt(LocalDateTime.of(2024,1,1,10,10))
            .build();
    post2.addMember(member);

    var post3 = Post.builder()
            .title("味噌ラーメン")
            .content("なら一蘭に行こう。ラーメンは豚骨だ。")
            .createdAt(LocalDateTime.of(2024,1,5,10,10))
            .build();
    post3.addMember(member);
    postRepository.saveAll(List.of(post1, post2, post3));

    // expected
    mockMvc.perform(get("/posts?title=ラーメン")
                    .contentType(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.isLast", is(true)))
            .andExpect(jsonPath("$.totalLength", is(2)))
            .andExpect(jsonPath("$.lastPage", is(1)))
            .andExpectAll(
                    jsonPath("$.postResponses.[0].title", is("味噌ラーメン")),
                    jsonPath("$.postResponses.[0].content", is("なら一蘭に行こう。ラーメンは豚骨だ。")),
                    jsonPath("$.postResponses.[0].author", is("テストメンバー"))
            )
            .andDo(print());
  }

  @Test
  @DisplayName("/posts 指定タグのポスト一覧を返す")
  void getPostsWithDuplicateHashTags() throws Exception {
    // given
    var member = Member.builder()
            .name("テストメンバー")
            .password("1q2w3e4r")
            .email("test@test.com")
            .build();
    var post1 = Post.builder()
            .title("豚骨ラーメン")
            .content("なら一蘭に行こう。ラーメンは豚骨だ。")
            .createdAt(LocalDateTime.of(2024,1,1,10,10))
            .build();
    post1.addMember(member);

    var post2 = Post.builder()
            .title("明太子パスタ")
            .content("なら一蘭に行こう。ラーメンは豚骨だ。")
            .createdAt(LocalDateTime.of(2024,1,1,10,10))
            .build();
    post2.addMember(member);

    var post3 = Post.builder()
            .title("味噌ラーメン")
            .content("なら一蘭に行こう。ラーメンは豚骨だ。")
            .createdAt(LocalDateTime.of(2024,1,5,10,10))
            .build();
    post3.addMember(member);

    var tagJava = HashTag.builder()
            .name("#Java")
            .build();
    var postHashTag1 = PostHashTag.builder()
            .build();
    postHashTag1.addPost(post1);
    postHashTag1.addHashTag(tagJava);

    var tagSpring = HashTag.builder()
            .name("#Spring")
            .build();
    var postHashTag2 = PostHashTag.builder()
            .build();
    postHashTag2.addPost(post2);
    postHashTag2.addHashTag(tagSpring);

    postRepository.saveAll(List.of(post1, post2, post3));

    // expected
    mockMvc.perform(get("/posts?hashTags=Java,Spring")
                    .contentType(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.isLast", is(true)))
            .andExpect(jsonPath("$.totalLength", is(2)))
            .andExpect(jsonPath("$.lastPage", is(1)))
            .andExpectAll(
                    jsonPath("$.postResponses.[0].title", is("明太子パスタ")),
                    jsonPath("$.postResponses.[0].content", is("なら一蘭に行こう。ラーメンは豚骨だ。")),
                    jsonPath("$.postResponses.[0].author", is("テストメンバー")),
                    jsonPath("$.postResponses.[0].hashTags.[0].name", is("#Spring"))
            )
            .andDo(print());
  }

  /**
   * Post更新
   */
  @CustomWithMockUser
  @Transactional
  @Test
  @DisplayName("/post/{postId} クライアントから受け取ったpostIdとポストの全ての値を元にポストを更新する")
  void updatePost() throws Exception {
    // given
    // 先に保存されたMockUserのデータを呼び出す
    var member = memberRepository.findAll().get(0);
    var post1 = Post.builder()
        .title("美味しいラーメンが食いたい。")
        .content("なら一蘭に行こう。ラーメンは豚骨だ。")
        .build();
    post1.addMember(member);
    var post2 = Post.builder()
        .title("美味しいラーメンが食いたい。")
        .content("なら一蘭に行こう。ラーメンは豚骨だ。")
        .build();
    var hashTag = HashTag.builder()
        .name("#ラーメン")
        .build();
    var postHashTag = PostHashTag.builder()
        .build();
    postHashTag.addPost(post2);
    postHashTag.addHashTag(hashTag);
    post2.addMember(member);
    postRepository.saveAll(List.of(post1, post2));

    var postUpdate = PostUpdate.builder()
        .title("今日は焼肉を食べよう")
        .content("美味しい焼肉店をGoogle Mapから探しましょう")
        .hashTags("#Spring#Java#Kakao")
        .build();
    String json = objectMapper.writeValueAsString(postUpdate);

    // when
    mockMvc.perform(MockMvcRequestBuilders.put("/post/{postId}", post2.getId())
            .contentType(APPLICATION_JSON)
            .content(json)
        ).andExpect(status().isOk())
        .andDo(print());

    // then
    var findPost = postRepository.findById(post2.getId()).get();
    assertThat(findPost.getTitle()).isEqualTo("今日は焼肉を食べよう");
    assertThat(findPost.getContent()).isEqualTo("美味しい焼肉店をGoogle Mapから探しましょう");
    var names = findPost.getPostHashTags().stream()
        .map(p -> p.getHashTag().getName());
    assertThat(names).hasSize(3).containsExactly("#Spring", "#Java", "#Kakao");
  }


}
