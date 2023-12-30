package jp.falsystack.falsylog_backend.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import java.util.List;
import jp.falsystack.falsylog_backend.config.UserPrincipal;
import jp.falsystack.falsylog_backend.request.post.PostCreate;
import jp.falsystack.falsylog_backend.request.post.PostSearch;
import jp.falsystack.falsylog_backend.request.post.PostUpdate;
import jp.falsystack.falsylog_backend.response.PostResponse;
import jp.falsystack.falsylog_backend.service.PostService;
import jp.falsystack.falsylog_backend.service.dto.PostWrite;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PostController {

  private final PostService postService;

  // TODO: クライアントからな要求でログインページが完了するまでは認証処理させない。
//  @PreAuthorize("hasRole('ADMIN')")
//  @PostMapping("/post")
//  public void createPost(@AuthenticationPrincipal UserPrincipal userPrincipal,
//      @RequestBody @Valid PostCreate postCreate) {
//    PostWrite postWrite = PostWrite.of(postCreate, userPrincipal.getUserId());
//    postService.write(postWrite);
//  }

  @PostMapping("/post")
  public void createPost(@AuthenticationPrincipal UserPrincipal userPrincipal,
      @RequestBody @Valid PostCreate postCreate) {
    // TODO: クライアントからな要求でログインページが完了するまでは認証処理させない。
    PostWrite postWrite = PostWrite.of(postCreate,
        userPrincipal == null ? 1L : userPrincipal.getUserId());
    postService.write(postWrite);
  }

  @GetMapping("/post/{postId}")
  public PostResponse getPost(
      @Valid @Positive @PathVariable Long postId) {
    return postService.getPost(postId);
  }

  // TODO: クライアントからな要求でログインページが完了するまでは認証処理させない。
  // @PreAuthorize("hasRole('ADMIN') && hasPermission(#postId, 'POST', 'DELETE')")
  @DeleteMapping("/post/{postId}")
  public void deletePost(@PathVariable Long postId) {
    postService.delete(postId);
  }

  @GetMapping("/posts")
  public List<PostResponse> getPosts(@ModelAttribute PostSearch request) {
    return postService.getPosts(request);
  }

  @PutMapping("/post/{postId}")
  public void update(
      @RequestBody @Valid PostUpdate postUpdate,
      @Valid @Positive @PathVariable Long postId) {
    postService.update(postId, postUpdate.toPostEdit());
  }

}
