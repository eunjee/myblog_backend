package jp.falsystack.falsylog_backend.apidocs;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.JsonFieldType.ARRAY;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import com.fasterxml.jackson.databind.ObjectMapper;
import jp.falsystack.falsylog_backend.service.PostService;
import jp.falsystack.falsylog_backend.service.dto.PostWrite;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@ExtendWith(RestDocumentationExtension.class)
public class TagControllerDocTest {

  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private ObjectMapper objectMapper;
  @Autowired
  private PostService postService;

  @Test
  @DisplayName("GET /tags/{tagName} ハッシュタグに関連するポストを返す")
  void getPostListWithTags() throws Exception {
    // given
    // コントローラーでのテストなので実はRepositoryに直接PostとHashTagを登録するのが良い方向だが。
    // 面倒臭くなるのでpostServiceでささっとうやる。
    postService.write(PostWrite.builder()
        .title("タグ付きポストのタイトルです。")
        .content("タグ付きポストのコンテンツです。")
        .author("ユーザー機能はいつ作るの。")
        .hashTags("#Spring#Java#javascript")
        .build());

    // expected
    mockMvc.perform(get("/tags/{tagName}", "Spring")
            .contentType(APPLICATION_JSON))
        .andDo(document("tags-getPosts",
            responseFields(
                fieldWithPath("[].id").type(NUMBER).description("ID"),
                fieldWithPath("[].author").type(STRING).description("作成者"),
                fieldWithPath("[].title").type(STRING).description("タイトル"),
                fieldWithPath("[].content").type(STRING).description("コンテンツ"),
                fieldWithPath("[].createdAt").type(STRING).description("作成日"),
                fieldWithPath("[].updatedAt").type(STRING).description("更新日"),
                fieldWithPath("[].hashTags").type(ARRAY).description("ハッシュタグのリスト"),
                fieldWithPath("[].hashTags.[].id").type(NUMBER).description("ハッシュタグのID"),
                fieldWithPath("[].hashTags.[].name").type(STRING).description("ハッシュタグ名")
            )
        )).andDo(print());
  }
}
