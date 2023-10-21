package jp.falsystack.falsylog_backend.controller;

import java.util.List;
import jp.falsystack.falsylog_backend.request.PostCreate;
import jp.falsystack.falsylog_backend.response.PostResponse;
import jp.falsystack.falsylog_backend.service.PostService;
import jp.falsystack.falsylog_backend.service.dto.PostWrite;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PostController {

  private final PostService postService;

  @PostMapping("/post")
  public void post(@RequestBody PostCreate postCreate) {
    PostWrite postWrite = PostWrite.from(postCreate);
    postService.write(postWrite);
  }

  @GetMapping("/post/{postId}")
  public PostResponse getPost(@PathVariable Long postId) {
    return postService.getPost(postId);
  }

  @GetMapping("/posts")
  public List<PostResponse> posts() {

    return postService.getPosts();
  }

}
