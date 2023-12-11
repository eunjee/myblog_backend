package jp.falsystack.falsylog_backend.apidocs;


import static org.mockito.Mockito.mock;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import jp.falsystack.falsylog_backend.annotation.CustomWithMockUser;
import jp.falsystack.falsylog_backend.apidocs.config.RestDocSupport;
import jp.falsystack.falsylog_backend.controller.PostController;
import jp.falsystack.falsylog_backend.request.post.PostCreate;
import jp.falsystack.falsylog_backend.service.PostService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;

public class PostControllerDocTest extends RestDocSupport {

  private final PostService postService = mock(PostService.class);

  @Override
  protected Object initController() {
    return new PostController(postService);
  }

  @CustomWithMockUser
  @Test
  @DisplayName("/post, ポスト登録")
  void postCreate() throws Exception {
    // given
    var request = PostCreate.builder()
        .title("美味しいラーメンが食いたい。")
        .content("なら一蘭に行こう。ラーメンは豚骨だ。")
        .hashTags("#一蘭#Spring#React")
        .build();
    var json = objectMapper.writeValueAsString(request);

    // expected
    mockMvc.perform(post("/post")
            .contentType(APPLICATION_JSON)
            .content(json))
        .andDo(document("post-create",
                preprocessRequest(prettyPrint()),
                requestFields(
                    fieldWithPath("title").type(STRING).description("제목"),
                    fieldWithPath("content").type(STRING).description("내용"),
                    fieldWithPath("hashTags").type(STRING).description("#Spring#Java#Javascript")
                        .optional()
                )
            )
        );
  }

  @CustomWithMockUser
  @Test
  @DisplayName("/post ポスト登録失敗 - タイトルは必須です")
  void postCreateFailedRequired() throws Exception {
    // given
    var request = PostCreate.builder()
        .title(null)
        .content(null)
        .hashTags("#一蘭#Spring#React")
        .build();
    String json = objectMapper.writeValueAsString(request);

    // expected
    mockMvc.perform(RestDocumentationRequestBuilders.post("/post")
            .contentType(APPLICATION_JSON)
            .content(json))
        .andDo(document("post-create-title-required",
            preprocessResponse(prettyPrint()),
            responseFields(
                fieldWithPath("message").type(STRING).description("잘못된 요청입니다"),
                fieldWithPath("validation.title").type(STRING)
                    .description("제목을 입력해주세요"),
                fieldWithPath("validation.content").type(STRING)
                    .description("내용을 입력해주세요")
            )))
        .andDo(print());
  }
//
//  @Test
//  @DisplayName("POST /post ポスト登録失敗 - 文字数制限")
//  void postCreateFailedTitle2() throws Exception {
//    // given
//    var request = createPostRequesOptionaltDto("", "内容1234", "作成者", null);
//    String json = objectMapper.writeValueAsString(request);
//
//    // expected
//    mockMvc.perform(RestDocumentationRequestBuilders.post("/post")
//            .contentType(APPLICATION_JSON)
//            .content(json))
//        .andDo(document("post-create-fail-length",
//            responseFields(
//                fieldWithPath("message").type(STRING).description("間違ったリクエストです。"),
//                fieldWithPath("validation.title").type(STRING)
//                    .description("タイトルは１文字以上２０文字以下で作成してください"),
//                fieldWithPath("validation.content").type(STRING)
//                    .description("１０文字以上作成してください。")
//            )))
//        .andDo(print());
//  }

//  @Test
//  @DisplayName("GET /post/{postId} ポスト詳細照会")
//  void getPostById() throws Exception {
//    // given
//    var postDto1 = createPostEntityOptional(1);
//    var savedPost = postRepository.save(postDto1);
//
//    // expected
//    mockMvc.perform(get("/post/{postId}", savedPost.getId())
//            .contentType(APPLICATION_JSON))
//        .andDo(document("post-detail",
//            preprocessRequest(prettyPrint()),
//            preprocessResponse(prettyPrint()),
//            RequestDocumentation.pathParameters(
//                RequestDocumentation.parameterWithName("postId").description("게시글ID") // ポストID
//            ),
//            responseFields(
//                fieldWithPath("id").type(NUMBER).description("ID")
//                    .attributes(Attributes.key("constraint").value("필수필수")),
//                fieldWithPath("title").type(STRING).description("타이틀"), // タイトル
//                fieldWithPath("content").type(STRING).description("컨텐츠"), // コンテンツ
//                fieldWithPath("author").type(STRING).description("작성자"), // 作成者
//                fieldWithPath("hashTags").type(ARRAY).description("hashtag 목록"),
//                fieldWithPath("hashTags.name").type(STRING).description("태그이름").optional(),
//                // hash tag name
//                fieldWithPath("createdAt").type(STRING).description("작성일"), // 作成日
//                fieldWithPath("updatedAt").type(STRING).description("갱신일") // 更新日
//            )
//        ))
//        .andDo(print());
//  }
//
//  @Test
//  @DisplayName("DELETE /post/{postId} ポスト一件削除")
//  void deletePostById() throws Exception {
//    // given
//    var postDto1 = createPostDto(1);
//    var savedPost = postRepository.save(postDto1);
//
//    // expected
//    mockMvc.perform(delete("/post/{postId}", savedPost.getId())
//            .contentType(APPLICATION_JSON))
//        .andDo(document("post-delete",
//            RequestDocumentation.pathParameters(
//                RequestDocumentation.parameterWithName("postId").description("ポストID(게시글ID)")
//            )
//        ))
//        .andDo(print());
//  }
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
