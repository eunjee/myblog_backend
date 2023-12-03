package jp.falsystack.falsylog_backend.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import jp.falsystack.falsylog_backend.annotation.CustomWithMockUser;
import jp.falsystack.falsylog_backend.domain.HashTag;
import jp.falsystack.falsylog_backend.domain.Post;
import jp.falsystack.falsylog_backend.domain.PostHashTag;
import jp.falsystack.falsylog_backend.repository.HashTagRepository;
import jp.falsystack.falsylog_backend.repository.PostRepository;
import jp.falsystack.falsylog_backend.request.PostCreate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@SpringBootTest
@AutoConfigureMockMvc
public class PostControllerTest {

  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private ObjectMapper objectMapper;
  @Autowired
  private PostRepository postRepository;
  @Autowired
  private HashTagRepository hashTagRepository;

  private static Post createPostEntity(int count) {
    return Post.builder()
        .title("Post Title" + count)
        .content("Content 1234" + count)
        .author("myblog author" + count)
        .build();
  }

  private static Post createPostEntityOptional(int count) {
    var post = Post.builder()
        .title("Post Title" + count)
        .content("Content 1234" + count)
        .author("myblog author" + count)
        .build();
    return post;
  }

  private static PostHashTag createPostHashTag(HashTag hashTag, Post post) {
    return PostHashTag.builder()
        .hashTag(hashTag)
        .post(post)
        .build();
  }

  private static HashTag createHashTag(String name) {
    return HashTag.builder()
        .name(name)
        .build();
  }

  private static PostCreate createPostRequestDto(int count) {
    return PostCreate.builder()
        .title("Post Title" + count)
        .content("Content 1234" + count)
        .author("myblog author" + count)
        .hashTags(null)
        .build();
  }

  private static PostCreate createPostRequestOptionalDto(String title, String content,
      String author, String hashTags) {
    return PostCreate.builder()
        .title(title)
        .content(content)
        .author(author)
        .hashTags(hashTags)
        .build();
  }

  @BeforeEach
  void beforeEach() {
    postRepository.deleteAll();
//    postRepository.deleteAllInBatch();
  }

  @Test
  @DisplayName("POST /post ブログポスト登録")
  void post() throws Exception {
    // given
    var request = createPostRequestDto(0);
    String json = objectMapper.writeValueAsString(request);

    // expected
    mockMvc.perform(MockMvcRequestBuilders.post("/post")
            .header("authorization", "myblog")
            .contentType(APPLICATION_JSON)
            .content(json))
        .andExpect(status().isOk())
        .andDo(print());
  }

  @Test
  @DisplayName("POST /post ポストを登録する時にhashtagがあればhashtagも一緒に登録される。")
  void postCreateWithHashTags() throws Exception {
    // given
    var request = createPostRequestOptionalDto("ハッシュタグ付きポスト",
        "ハッシュタグ機能が出来ました。", "作成者",
        "#spring#java#Spring");
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
        "#spring", "#Spring", "#java"
    );
  }

  @Test
  @DisplayName("POST /post タイトルがないとブログポスト登録に失敗する。")
  void postFail_No_Title() throws Exception {
    // given
    var request = createPostRequestOptionalDto(null, "内容12345678", "作成者", null);
    String json = objectMapper.writeValueAsString(request);

    // expected
    mockMvc.perform(MockMvcRequestBuilders.post("/post")
            .contentType(APPLICATION_JSON)
            .content(json))
        .andExpect(status().isBadRequest())
        .andExpectAll(
            jsonPath("$.message", is("잘못된 요청입니다.")),
            jsonPath("$.validation.title", is("타이틀을 입력해주세요"))
        )
        .andDo(print());
  }

  @Test
  @DisplayName("POST /post タイトルは２０文字以下で入力しないとポスト登録に失敗する。")
  void postFail_Least_One_Word() throws Exception {
    // given
    var request = createPostRequestOptionalDto("1234567890,1234567890", "内容12345678", "作成者",
        null);
    String json = objectMapper.writeValueAsString(request);

    // expected
    mockMvc.perform(MockMvcRequestBuilders.post("/post")
            .contentType(APPLICATION_JSON)
            .content(json))
        .andExpect(status().isBadRequest())
        .andExpectAll(
            jsonPath("$.message", is("잘못된 요청입니다.")),
            jsonPath("$.validation.title", is("타이틀은 한 글자 이상 20 자 이하로 작성해 주세요"))
        )
        .andDo(print());
  }

  @Test
  @DisplayName("POST /post Content がないとブログポスト登録に失敗する。")
  void postFailContentMustBeNotBlank() throws Exception {
    // given
    var request = createPostRequestOptionalDto("タイトル", null, "作成者", null);
    String json = objectMapper.writeValueAsString(request);

    // expected
    mockMvc.perform(MockMvcRequestBuilders.post("/post")
            .contentType(APPLICATION_JSON)
            .content(json))
        .andExpect(status().isBadRequest())
        .andExpectAll(
            jsonPath("$.message", is("잘못된 요청입니다.")),
            jsonPath("$.validation.content", is("내용을 입력해주세요"))
        )
        .andDo(print());
  }

  @Test
  @DisplayName("POST /post Content は１０文字以上入力しないとポスト登録に失敗する。")
  void postFailContentMustBeLeastTenWord() throws Exception {
    // given
    var request = createPostRequestOptionalDto("タイトル", "内容1234567", "作成者", null);
    String json = objectMapper.writeValueAsString(request);

    // expected
    mockMvc.perform(MockMvcRequestBuilders.post("/post")
            .contentType(APPLICATION_JSON)
            .content(json))
        .andExpect(status().isBadRequest())
        .andExpectAll(
            jsonPath("$.message", is("잘못된 요청입니다.")),
            jsonPath("$.validation.content", is("10글자 이상 입력해주세요"))
        )
        .andDo(print());
  }

  @Test
  @DisplayName("GET /post/{postId} ポストのIDで照会するとポストの詳細が返ってくる。")
  void getPost() throws Exception {
    // given
    var post = createPostEntity(0);
    var savedPost = postRepository.save(post);

    // expected
    mockMvc.perform(get("/post/{postId}", savedPost.getId())
            .contentType(APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id", is(savedPost.getId().intValue())))
        .andExpect(jsonPath("$.title", is("Post Title0")))
        .andExpect(jsonPath("$.content", is("Content 12340")))
        .andExpect(jsonPath("$.author", is("myblog author0")))
        .andDo(print());
  }

  @Test
  @DisplayName("GET /post/{postId} ポストのIDで照会するとポストの詳細が返ってくる。")
  void getPostWithHashTags() throws Exception {
    // given
    var post = createPostEntityOptional(0);
    var hashTag = createHashTag("#Spring");
    var postHashTag = createPostHashTag(hashTag, post);
    post.addPostHashTags(List.of(postHashTag));
    hashTag.addPostHashTags(List.of(postHashTag));
    var savedPost = postRepository.save(post);

    // expected
    mockMvc.perform(get("/post/{postId}", savedPost.getId())
            .contentType(APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id", is(savedPost.getId().intValue())))
        .andExpect(jsonPath("$.title", is("Post Title0")))
        .andExpect(jsonPath("$.content", is("Content 12340")))
        .andExpect(jsonPath("$.author", is("myblog author0")))
        .andExpect(jsonPath("$.hashTags.[0].name", is("#Spring")))
        .andDo(print());
  }


  @Test
  @DisplayName("DELETE /post/{postId} ポストのIDを元に削除を行う")
  void deletePost() throws Exception {
    // given
    var post = createPostEntity(0);
    var savedPost = postRepository.save(post);

    // expected
    mockMvc.perform(MockMvcRequestBuilders.delete("/post/{potId}", savedPost.getId())
            .contentType(APPLICATION_JSON))
        .andExpect(status().isOk())
        .andDo(print());
  }

  @Test
  @DisplayName("GET /posts ポスト一覧を返す")
  void getPosts() throws Exception {
    // given
    var postDto1 = createPostEntity(1);
    var postDto2 = createPostEntity(2);
    var postDto3 = createPostEntity(3);
    postRepository.saveAll(List.of(postDto1, postDto2, postDto3));

    // expected
    mockMvc.perform(get("/posts")
            .contentType(APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()", is(3)))
        .andExpectAll(
            jsonPath("$[0].title", is("Post Title3")),
            jsonPath("$[0].content", is("Content 12343")),
            jsonPath("$[0].author", is("myblog author3"))
        )
        .andDo(print());
  }


}
