package jp.falsystack.falsylog_backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import jp.falsystack.falsylog_backend.domain.Post;
import jp.falsystack.falsylog_backend.repository.PostRepository;
import jp.falsystack.falsylog_backend.request.PostCreate;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
public class PostControllerTest {

  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private ObjectMapper objectMapper;
  @Autowired
  private PostRepository postRepository;

  @BeforeEach
  void beforeEach() {
    postRepository.deleteAllInBatch();
  }

  private static Post createPostDto(int count) {
    return Post.builder()
        .title("記事タイトル" + count)
        .content("コンテンツ" + count)
        .author("falsystack" + count)
        .build();
  }

  private static PostCreate createPostRequestDto(int count) {
    return PostCreate.builder()
        .title("記事タイトル" + count)
        .content("コンテンツ" + count)
        .author("falsystack" + count)
        .build();
  }

  @Test
  @DisplayName("ブログ記事登録")
  void post() throws Exception {
    // given
    var request = createPostRequestDto(0);
    String json = objectMapper.writeValueAsString(request);

    // expected
    mockMvc.perform(MockMvcRequestBuilders.post("/post")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andDo(MockMvcResultHandlers.print());
  }

  @Test
  @DisplayName("GET /postsに要請するとメインページの記事一覧を返す")
  void getPosts() throws Exception {
    // given
    var postDto1 = createPostDto(1);
    var postDto2 = createPostDto(2);
    var postDto3 = createPostDto(3);
    postRepository.saveAll(List.of(postDto1, postDto2, postDto3));

    // expected
    mockMvc.perform(MockMvcRequestBuilders.get("/posts")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.length()", Matchers.is(3)))
        .andExpectAll(
            MockMvcResultMatchers.jsonPath("$[0].title", Matchers.is("記事タイトル1")),
            MockMvcResultMatchers.jsonPath("$[0].content", Matchers.is("コンテンツ1")),
            MockMvcResultMatchers.jsonPath("$[0].author", Matchers.is("falsystack1"))
        )
        .andDo(MockMvcResultHandlers.print());

  }


}
