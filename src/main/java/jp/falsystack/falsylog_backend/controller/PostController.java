package jp.falsystack.falsylog_backend.controller;

import jakarta.validation.Valid;
import java.util.List;
import jp.falsystack.falsylog_backend.request.PostCreate;
import jp.falsystack.falsylog_backend.request.PostSearch;
import jp.falsystack.falsylog_backend.response.PostResponse;
import jp.falsystack.falsylog_backend.service.PostService;
import jp.falsystack.falsylog_backend.service.dto.PostWrite;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
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
  public void createPost(@RequestBody @Valid PostCreate postCreate) {
    PostWrite postWrite = PostWrite.from(postCreate);
    postService.write(postWrite);
  }

  @GetMapping("/post/{postId}")
  public PostResponse getPost(@PathVariable Long postId) {
    return postService.getPost(postId);
  }

  @DeleteMapping("/post/{postId}")
  public void deletePost(@PathVariable Long postId) {
    postService.delete(postId);
  }

  @GetMapping("/posts")
  public List<PostResponse> getPosts(@ModelAttribute PostSearch request) {
    return postService.getPosts(request);
  }

}
