package jp.falsystack.falsylog_backend.apidocs;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.document;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import jp.falsystack.falsylog_backend.domain.Post;
import jp.falsystack.falsylog_backend.repository.PostRepository;
import jp.falsystack.falsylog_backend.request.PostCreate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.PayloadDocumentation;
import org.springframework.restdocs.request.RequestDocumentation;
import org.springframework.test.web.servlet.MockMvc;

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

  private static Post createPostDto(int count) {
    return Post.builder()
        .title("記事タイトル" + count)
        .content("コンテンツ" + count)
        .author("falsystack" + count)
        .build();
  }

  @BeforeEach
  void beforeEach() {
    postRepository.deleteAllInBatch();
  }

  @Test
  @DisplayName("POST /post, ポスト登録")
  void postCreate() throws Exception {
    // given
    var request = PostCreate.builder()
        .title("ポストータイトル(포스트 타이틀)")
        .content("ポスト内容(포스트 내용)")
        .author("作成者(작성자)")
        .build();
    var json = objectMapper.writeValueAsString(request);

    // expected
    mockMvc.perform(post("/post")
            .contentType(APPLICATION_JSON)
            .content(json))
        .andDo(
            document("post-create",
                requestFields(
                    fieldWithPath("title").type(STRING)
                        .description("タイトル(타이틀)"),
                    fieldWithPath("content").type(STRING)
                        .description("コンテンツ(컨텐츠)"),
                    fieldWithPath("author").type(STRING)
                        .description("作成者(작성자)")
                )
            )
        )
        .andDo(print());

  }

  @Test
  @DisplayName("GET /post/{postId} ポスト詳細照会")
  void getPostById() throws Exception {
    // given
    var postDto1 = createPostDto(1);
    var savedPost = postRepository.save(postDto1);

    // expected
    mockMvc.perform(get("/post/{postId}", savedPost.getId())
            .contentType(APPLICATION_JSON))
        .andDo(document("post-detail",
            RequestDocumentation.pathParameters(
                RequestDocumentation.parameterWithName("postId").description("ポストID(게시글ID)")
            ),
            responseFields(
                fieldWithPath("id").type(NUMBER).description("ID"),
                fieldWithPath("title").type(STRING).description("タイトル(타이틀)"),
                fieldWithPath("content").type(STRING).description("コンテンツ(컨텐츠)"),
                fieldWithPath("author").type(STRING).description("作成者(작성자)")
            )
        ))
        .andDo(print());
  }

  @Test
  @DisplayName("DELETE /post/{postId} ポスト一件削除")
  void deletePostById() throws Exception {
    // given
    var postDto1 = createPostDto(1);
    var savedPost = postRepository.save(postDto1);

    // expected
    mockMvc.perform(delete("/post/{postId}", savedPost.getId())
            .contentType(APPLICATION_JSON))
        .andDo(document("post-delete",
            RequestDocumentation.pathParameters(
                RequestDocumentation.parameterWithName("postId").description("ポストID(게시글ID)")
            )
        ))
        .andDo(print());
  }

  @Test
  @DisplayName("GET /posts,　ポスト一覧")
  void getPosts() throws Exception {
    // given
    var postDto1 = createPostDto(1);
    var postDto2 = createPostDto(2);
    var postDto3 = createPostDto(3);
    postRepository.saveAll(List.of(postDto1, postDto2, postDto3));

    // expected
    mockMvc.perform(get("/posts")
            .contentType(APPLICATION_JSON))
        .andDo(
            document("post-list",
                responseFields(
                    fieldWithPath("[].id").type(NUMBER)
                        .description("id(아이디)"),
                    fieldWithPath("[].title").type(STRING)
                        .description("タイトル(타이틀)"),
                    fieldWithPath("[].content").type(STRING)
                        .description("コンテンツ(컨텐츠)"),
                    fieldWithPath("[].author").type(STRING)
                        .description("作成者(작성자)")
                )
            )
        )
        .andDo(print());

  }
}
