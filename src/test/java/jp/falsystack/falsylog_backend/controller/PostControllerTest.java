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
import jp.falsystack.falsylog_backend.domain.Post;
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
        .title("記事タイトル" + count)
        .content("コンテンツ1234" + count)
        .author("falsystack" + count)
        .build();
  }

  private static Post createPostEntityOptional(int count) {
    var post = Post.builder()
        .title("記事タイトル" + count)
        .content("コンテンツ1234" + count)
        .author("falsystack" + count)
        .build();
    post.addPostHashTags("#Spring#Java#Javascript");
    return post;
  }

  private static PostCreate createPostRequestDto(int count) {
    return PostCreate.builder()
        .title("記事タイトル" + count)
        .content("コンテンツ1234" + count)
        .author("falsystack" + count)
        .hashTags(null)
        .build();
  }

  private static PostCreate createPostRequesOptionaltDto(String title, String content,
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
  @DisplayName("POST /post ブログ記事登録")
  void post() throws Exception {
    // given
    var request = createPostRequestDto(0);
    String json = objectMapper.writeValueAsString(request);

    // expected
    mockMvc.perform(MockMvcRequestBuilders.post("/post")
            .contentType(APPLICATION_JSON)
            .content(json))
        .andExpect(status().isOk())
        .andDo(print());
  }

  @Test
  @DisplayName("POST /post 記事を登録する時にhashtagがあればhashtagも一緒に登録される。")
  void postCreateWithHashTags() throws Exception {
    // given
    var request = createPostRequesOptionaltDto("ハッシュタグ付き記事",
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
  @DisplayName("POST /post タイトルがないとブログ記事登録に失敗する。")
  void postFail_No_Title() throws Exception {
    // given
    var request = createPostRequesOptionaltDto(null, "内容12345678", "作成者", null);
    String json = objectMapper.writeValueAsString(request);

    // expected
    mockMvc.perform(MockMvcRequestBuilders.post("/post")
            .contentType(APPLICATION_JSON)
            .content(json))
        .andExpect(status().isBadRequest())
        .andExpectAll(
            jsonPath("$.code", is("400")),
            jsonPath("$.message", is("間違ったリクエストです。")),
            jsonPath("$.validation.title", is("タイトルを入力してください。"))
        )
        .andDo(print());
  }

  @Test
  @DisplayName("POST /post タイトルは２０文字以下で入力しないと記事登録に失敗する。")
  void postFail_Least_One_Word() throws Exception {
    // given
    var request = createPostRequesOptionaltDto("1234567890,1234567890", "内容12345678", "作成者",
        null);
    String json = objectMapper.writeValueAsString(request);

    // expected
    mockMvc.perform(MockMvcRequestBuilders.post("/post")
            .contentType(APPLICATION_JSON)
            .content(json))
        .andExpect(status().isBadRequest())
        .andExpectAll(
            jsonPath("$.code", is("400")),
            jsonPath("$.message", is("間違ったリクエストです。")),
            jsonPath("$.validation.title", is("タイトルは１文字以上２０文字以下で作成してください。"))
        )
        .andDo(print());
  }

  @Test
  @DisplayName("POST /post コンテンツがないとブログ記事登録に失敗する。")
  void postFailContentMustBeNotBlank() throws Exception {
    // given
    var request = createPostRequesOptionaltDto("タイトル", null, "作成者", null);
    String json = objectMapper.writeValueAsString(request);

    // expected
    mockMvc.perform(MockMvcRequestBuilders.post("/post")
            .contentType(APPLICATION_JSON)
            .content(json))
        .andExpect(status().isBadRequest())
        .andExpectAll(
            jsonPath("$.code", is("400")),
            jsonPath("$.message", is("間違ったリクエストです。")),
            jsonPath("$.validation.content", is("内容を入力してください。"))
        )
        .andDo(print());
  }

  @Test
  @DisplayName("POST /post コンテンツは１０文字以上入力しないと記事登録に失敗する。")
  void postFailContentMustBeLeastTenWord() throws Exception {
    // given
    var request = createPostRequesOptionaltDto("タイトル", "内容1234567", "作成者", null);
    String json = objectMapper.writeValueAsString(request);

    // expected
    mockMvc.perform(MockMvcRequestBuilders.post("/post")
            .contentType(APPLICATION_JSON)
            .content(json))
        .andExpect(status().isBadRequest())
        .andExpectAll(
            jsonPath("$.code", is("400")),
            jsonPath("$.message", is("間違ったリクエストです。")),
            jsonPath("$.validation.content", is("１０文字以上作成してください。"))
        )
        .andDo(print());
  }

  @Test
  @DisplayName("GET /post/{postId} 記事のIDで照会すると記事の詳細が返ってくる。")
  void getPost() throws Exception {
    // given
    var post = createPostEntity(0);
    var savedPost = postRepository.save(post);

    // expected
    mockMvc.perform(get("/post/{postId}", savedPost.getId())
            .contentType(APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id", is(savedPost.getId().intValue())))
        .andExpect(jsonPath("$.title", is("記事タイトル0")))
        .andExpect(jsonPath("$.content", is("コンテンツ12340")))
        .andExpect(jsonPath("$.author", is("falsystack0")))
        .andDo(print());
  }

  @Test
  @DisplayName("GET /post/{postId} 記事のIDで照会すると記事の詳細が返ってくる。")
  void getPostWithHashTags() throws Exception {
    // given
    var post = createPostEntityOptional(0);
    var savedPost = postRepository.save(post);

    // expected
    mockMvc.perform(get("/post/{postId}", savedPost.getId())
            .contentType(APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id", is(savedPost.getId().intValue())))
        .andExpect(jsonPath("$.title", is("記事タイトル0")))
        .andExpect(jsonPath("$.content", is("コンテンツ12340")))
        .andExpect(jsonPath("$.author", is("falsystack0")))
        .andExpect(jsonPath("$.hashTags.[0].name", is("#Spring")))
        .andDo(print());
  }


  @Test
  @DisplayName("DELETE /post/{postId} 記事のIDを元に削除を行う")
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
  @DisplayName("GET /posts 記事一覧を返す")
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
            jsonPath("$[0].title", is("記事タイトル1")),
            jsonPath("$[0].content", is("コンテンツ12341")),
            jsonPath("$[0].author", is("falsystack1"))
        )
        .andDo(print());
  }


}
