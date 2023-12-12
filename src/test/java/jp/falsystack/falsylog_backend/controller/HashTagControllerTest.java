package jp.falsystack.falsylog_backend.controller;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import jp.falsystack.falsylog_backend.domain.HashTag;
import jp.falsystack.falsylog_backend.domain.Post;
import jp.falsystack.falsylog_backend.domain.PostHashTag;
import jp.falsystack.falsylog_backend.repository.post.PostRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

@AutoConfigureMockMvc
@SpringBootTest
class HashTagControllerTest {

  @Autowired
  private PostRepository postRepository;
  @Autowired
  private MockMvc mockMvc;

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

  private static Post createPostEntityOptional(int count) {
    var post = Post.builder()
        .title("記事タイトル" + count)
        .content("コンテンツ1234" + count)
//        .author("falsystack" + count)
        .build();
//    post.addPostHashTags("#Spring#Java#Javascript");
    return post;
  }

  @Disabled
  @Test
  @DisplayName("GET /hashtags/hashtagName HashTag名で照会するとHashTag名と係があるポストのリストを返す。")
  void getPostListIncludeHashTag() throws Exception {
    // given
    var spring = createHashTag("#Spring");
    var java = createHashTag("#Java");
    var javascript = createHashTag("#Javascript");

    var post1 = createPostEntityOptional(0);

    var postHashTag1 = createPostHashTag(spring, post1);
    var postHashTag2 = createPostHashTag(java, post1);
    var postHashTag3 = createPostHashTag(javascript, post1);
    post1.addPostHashTags(List.of(postHashTag1, postHashTag2, postHashTag3));

    var post2 = createPostEntityOptional(1);

    var postHashTag4 = createPostHashTag(spring, post2);
    var postHashTag5 = createPostHashTag(java, post2);
    var postHashTag6 = createPostHashTag(javascript, post2);
    post2.addPostHashTags(List.of(postHashTag4, postHashTag5, postHashTag6));

    var post3 = createPostEntityOptional(2);

    var postHashTag7 = createPostHashTag(spring, post3);
    var postHashTag8 = createPostHashTag(java, post3);
    var postHashTag9 = createPostHashTag(javascript, post3);
    post3.addPostHashTags(List.of(postHashTag7, postHashTag8, postHashTag9));

    spring.addPostHashTags(List.of(postHashTag1, postHashTag4, postHashTag7));
    java.addPostHashTags(List.of(postHashTag2, postHashTag5, postHashTag8));
    javascript.addPostHashTags(List.of(postHashTag3, postHashTag6, postHashTag9));

    postRepository.saveAll(List.of(post1, post2, post3));

    // expected
    mockMvc.perform(MockMvcRequestBuilders.get("/tags/{tagName}", "Spring"))
        .andExpect(status().isOk())
        .andExpectAll(
            jsonPath("$[0].id", is(post3.getId().intValue())),
            jsonPath("$[0].title", is("記事タイトル2")),
            jsonPath("$[0].content", is("コンテンツ12342")),
            jsonPath("$[0].author", is("falsystack2"))
        )
        .andDo(MockMvcResultHandlers.print());

  }

}