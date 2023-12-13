package jp.falsystack.falsylog_backend.apidocs;


import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.JsonFieldType.NULL;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.PersistenceContext;
import jp.falsystack.falsylog_backend.annotation.CustomWithMockUser;
import jp.falsystack.falsylog_backend.domain.Member;
import jp.falsystack.falsylog_backend.domain.Post;
import jp.falsystack.falsylog_backend.repository.MemberRepository;
import jp.falsystack.falsylog_backend.repository.post.PostRepository;
import jp.falsystack.falsylog_backend.request.post.PostCreate;
import jp.falsystack.falsylog_backend.service.dto.PostWrite;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.request.RequestDocumentation;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureRestDocs
@AutoConfigureMockMvc
public class PostControllerDocTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private PostRepository postRepository;

  @Autowired
  private MemberRepository memberRepository;

  @Autowired
  private ObjectMapper objectMapper;


  @AfterEach
  void tearDown() {
    postRepository.deleteAllInBatch();
    memberRepository.deleteAllInBatch();
  }

  @CustomWithMockUser
  @Test
  @DisplayName("/post ポスト登録")
  void postCreate() throws Exception {
    // given
    var request = PostCreate.builder()
        .title("새로운 리액트 문서에서 제시하는 9가지 권장 사항")
        .content(
            "일반적으로 새로운 소프트웨어 개발팀이 구성되면 팀이 코드를...")
        .hashTags("#react#js#ts")
        .build();
    var json = objectMapper.writeValueAsString(request);

    // expected
    mockMvc.perform(post("/post")
            .contentType(APPLICATION_JSON)
            .content(json))
        .andExpect(status().isOk())
        .andDo(document("post-create",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint()),
                requestFields(
                    fieldWithPath("title").type(STRING).description("제목")
                        .attributes(key("constraint").value("1~30자 이내")),
                    fieldWithPath("content").type(STRING).description("내용")
                        .attributes(key("constraint").value("10자 미만")),
                    fieldWithPath("hashTags").type(STRING).description("해시태그")
                        .optional()
                )
            )
        );
  }

  /**
   * title 検証
   */

  @CustomWithMockUser
  @Test
  @DisplayName("/post ポスト登録失敗 - タイトルは必須です")
  void postCreateFailedRequired() throws Exception {
    // given
    var request = PostCreate.builder()
        .title(null)
        .content("일반적으로 새로운 소프트웨어 개발팀이 구성되면 팀이 코드를...")
        .hashTags("#react#js#ts")
        .build();
    String json = objectMapper.writeValueAsString(request);

    // expected
    mockMvc.perform(RestDocumentationRequestBuilders.post("/post")
            .contentType(APPLICATION_JSON)
            .content(json))
        .andDo(document("post-create-title-required",
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            requestFields(
                fieldWithPath("title").type(NULL).description("제목")
                    .attributes(key("constraint").value("1~30자 이내")),
                fieldWithPath("content").type(STRING).description("내용")
                    .attributes(key("constraint").value("10자 미만")),
                fieldWithPath("hashTags").type(STRING).description("해시태그")
                    .optional()
            ),
            responseFields(
                fieldWithPath("message").type(STRING).description("에러 메시지"),
                fieldWithPath("validation.title").type(STRING)
                    .description("검증 실패 내용")
            )))
        .andDo(print());
  }

  @CustomWithMockUser
  @Test
  @DisplayName("/post ポスト登録失敗 - タイトルは1~30文字で入力してください")
  void postCreateFailedLength() throws Exception {
    // given
    var request = PostCreate.builder()
        .title("새로운 리액트 문서에서 제시하는 9가지 권장 사항을 알아보자")
        .content("일반적으로 새로운 소프트웨어 개발팀이 구성되면 팀이 코드를...")
        .hashTags("#react#js#ts")
        .build();
    String json = objectMapper.writeValueAsString(request);

    // expected
    mockMvc.perform(RestDocumentationRequestBuilders.post("/post")
            .contentType(APPLICATION_JSON)
            .content(json))
        .andDo(document("post-create-title-length",
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            requestFields(
                fieldWithPath("title").type(STRING).description("제목")
                    .attributes(key("constraint").value("1~30자 이내")),
                fieldWithPath("content").type(STRING).description("내용")
                    .attributes(key("constraint").value("10자 미만")),
                fieldWithPath("hashTags").type(STRING).description("해시태그")
                    .optional()
            ),
            responseFields(
                fieldWithPath("message").type(STRING).description("에러 메시지"),
                fieldWithPath("validation.title").type(STRING)
                    .description("검증 실패 내용")
            )))
        .andDo(print());
  }

  /**
   * content 検証
   */

  @CustomWithMockUser
  @Test
  @DisplayName("/post ポスト登録失敗 - コンテンツは必須です")
  void postCreateFailedContentRequired() throws Exception {
    // given
    var request = PostCreate.builder()
        .title("새로운 리액트 문서에서 제시하는 9가지 권장 사항")
        .content(null)
        .hashTags("#react#js#ts")
        .build();
    String json = objectMapper.writeValueAsString(request);

    // expected
    mockMvc.perform(RestDocumentationRequestBuilders.post("/post")
            .contentType(APPLICATION_JSON)
            .content(json))
        .andDo(document("post-create-content-required",
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            requestFields(
                fieldWithPath("title").type(STRING).description("제목")
                    .attributes(key("constraint").value("1~30자 이내")),
                fieldWithPath("content").type(NULL).description("내용")
                    .attributes(key("constraint").value("10자 미만")),
                fieldWithPath("hashTags").type(STRING).description("해시태그")
                    .optional()
            ),
            responseFields(
                fieldWithPath("message").type(STRING).description("에러 메시지"),
                fieldWithPath("validation.content").type(STRING)
                    .description("검증 실패 내용")
            )))
        .andDo(print());
  }

  @CustomWithMockUser
  @Test
  @DisplayName("/post ポスト登録失敗 - コンテンツは10文字以上で入力してください")
  void postCreateFailedContentLength() throws Exception {
    // given
    var request = PostCreate.builder()
        .title("새로운 리액트 문서에서 제시하는 9가지 권장 사항")
        .content("일반적으로 새로운")
        .hashTags("#react#js#ts")
        .build();
    String json = objectMapper.writeValueAsString(request);

    // expected
    mockMvc.perform(RestDocumentationRequestBuilders.post("/post")
            .contentType(APPLICATION_JSON)
            .content(json))
        .andDo(document("post-create-content-length",
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            requestFields(
                fieldWithPath("title").type(STRING).description("제목")
                    .attributes(key("constraint").value("1~30자 이내")),
                fieldWithPath("content").type(STRING).description("내용")
                    .attributes(key("constraint").value("10자 미만")),
                fieldWithPath("hashTags").type(STRING).description("해시태그")
                    .optional()
            ),
            responseFields(
                fieldWithPath("message").type(STRING).description("에러 메시지"),
                fieldWithPath("validation.content").type(STRING)
                    .description("검증 실패 내용")
            )))
        .andDo(print());
  }

  /**
   * 削除
   */
  @Transactional
  @CustomWithMockUser
  @Test
  @DisplayName("DELETE /post/{postId} ポスト削除")
  void deleteById() throws Exception {
    // given
    var member = memberRepository.findAll().get(0);

    var post = Post.builder()
        .title("새로운 리액트 문서에서 제시하는 9가지 권장 사항")
        .content(
            "일반적으로 새로운 소프트웨어 개발팀이 구성되면 팀이 코드를...")
        .build();
    post.addMember(member);
    var savedPost = postRepository.save(post);

    // expected
    mockMvc.perform(delete("/post/{postId}", savedPost.getId()))
        .andExpect(status().isOk())
        .andDo(document("post-delete",
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            RequestDocumentation.pathParameters(
                RequestDocumentation
                    .parameterWithName("postId")
                    .description("게시글 ID")
                    .attributes(key("constraint").value("NOT NULL, Long"))
            )
        )).andDo(print());

  }

//

//
//  @Test
//  @DisplayName("GET /posts,　ポスト一覧")
//  @Transactional
//  void getPosts() throws Exception {
//    // given
//    var postDto1 = createPostDto(1);
//    var postDto2 = createPostDto(2);
//    var postDto3 = createPostDto(3);
//    postRepository.saveAll(List.of(postDto1, postDto2, postDto3));
//
//    // expected
//    mockMvc.perform(get("/posts")
//            .contentType(APPLICATION_JSON))
//        .andDo(
//            document("post-list",
//                responseFields(
//                    fieldWithPath("[].id").type(NUMBER).description("id(아이디)"),
//                    fieldWithPath("[].title").type(STRING).description("タイトル(타이틀)"),
//                    fieldWithPath("[].content").type(STRING).description("コンテンツ(컨텐츠)"),
//                    fieldWithPath("[].author").type(STRING).description("作成者(작성자)"),
//                    fieldWithPath("[].hashTags").type(ARRAY).ignored().optional(),
//                    fieldWithPath("[].createdAt").type(STRING).description("作成日(작성일)"),
//                    fieldWithPath("[].updatedAt").type(STRING).description("更新日(갱신일)")
//                )
//            )
//        )
//        .andDo(print());
//
//  }
}
