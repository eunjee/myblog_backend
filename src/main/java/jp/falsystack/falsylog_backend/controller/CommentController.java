package jp.falsystack.falsylog_backend.controller;

import jakarta.validation.Valid;
import jp.falsystack.falsylog_backend.request.CommentCreate;
import jp.falsystack.falsylog_backend.request.CommentDelete;
import jp.falsystack.falsylog_backend.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CommentController {

  private final CommentService commentService;

  @PostMapping("/posts/{postId}/comments")
  public void write(@PathVariable Long postId, @RequestBody @Valid CommentCreate request) {
    // TODO: service에 맞는 dto로 변환해서 넘기기
    commentService.write(postId, request);
  }

  // SPECに2022年6月にDELETEには HTTP Request Bodyは使用しない事が明示された
  // @DeleteMapping("/comments/{commentId}")
  @PostMapping("/comments/{commentId}/delete")
  public void delete(@PathVariable Long commentId, @RequestBody @Valid CommentDelete request) {
    commentService.delete(commentId, request);
  }
}
