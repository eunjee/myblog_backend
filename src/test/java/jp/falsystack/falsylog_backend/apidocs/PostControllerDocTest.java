package jp.falsystack.falsylog_backend.apidocs;

import com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import jp.falsystack.falsylog_backend.domain.Post;
import jp.falsystack.falsylog_backend.repository.PostRepository;
import jp.falsystack.falsylog_backend.request.PostCreate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.payload.PayloadDocumentation;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@ExtendWith(RestDocumentationExtension.class)
public class PostControllerDocTest {

  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private ObjectMapper objectMapper;
  @Autowired
  private PostRepository postRepository;

  @Test
  @DisplayName("post要請で受け取ったパラメーターでポストが正常に登録される。")
  void postCreate() throws Exception {
    // given
    var request = PostCreate.builder()
        .title("ポストータイトル(포스트 타이틀)")
        .content("ポスト内容(포스트 내용)")
        .author("作成者(작성자)")
        .build();
    var json = objectMapper.writeValueAsString(request);

    // expected
    mockMvc.perform(RestDocumentationRequestBuilders.post("/post")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
        .andDo(
            MockMvcRestDocumentationWrapper.document("post-create",
                PayloadDocumentation.requestFields(
                    PayloadDocumentation.fieldWithPath("title").type(JsonFieldType.STRING).description("タイトル(타이틀)"),
                    PayloadDocumentation.fieldWithPath("content").type(JsonFieldType.STRING).description("コンテンツ(컨텐츠)"),
                    PayloadDocumentation.fieldWithPath("author").type(JsonFieldType.STRING).description("作成者(작성자)")
                )
            )
        )
        .andDo(MockMvcResultHandlers.print());

  }

  @Test
  @DisplayName("GET /posts要請するとポストリストが返ってくる")
  void getPosts() throws Exception {
    // given
    var postDto1 = createPostDto(1);
    var postDto2 = createPostDto(2);
    var postDto3 = createPostDto(3);
    postRepository.saveAll(List.of(postDto1, postDto2, postDto3));

    // expected
    mockMvc.perform(RestDocumentationRequestBuilders.get("/posts")
            .contentType(MediaType.APPLICATION_JSON))
        .andDo(
            MockMvcRestDocumentationWrapper.document("post-list",
                PayloadDocumentation.responseFields(
                    PayloadDocumentation.fieldWithPath("[].id").type(JsonFieldType.NUMBER).description("id(아이디)"),
                    PayloadDocumentation.fieldWithPath("[].title").type(JsonFieldType.STRING).description("タイトル(타이틀)"),
                    PayloadDocumentation.fieldWithPath("[].content").type(JsonFieldType.STRING).description("コンテンツ(컨텐츠)"),
                    PayloadDocumentation.fieldWithPath("[].author").type(JsonFieldType.STRING).description("作成者(작성자)")
                )
            )
        )
        .andDo(MockMvcResultHandlers.print());

  }

  private static Post createPostDto(int count) {
    return Post.builder()
        .title("記事タイトル" + count)
        .content("コンテンツ" + count)
        .author("falsystack" + count)
        .build();
  }
}
