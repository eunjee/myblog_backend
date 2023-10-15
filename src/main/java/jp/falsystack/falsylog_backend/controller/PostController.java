package jp.falsystack.falsylog_backend.controller;

import jp.falsystack.falsylog_backend.request.PostCreate;
import jp.falsystack.falsylog_backend.service.PostService;
import jp.falsystack.falsylog_backend.service.dto.PostWrite;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PostController {

  private final PostService postService;

  @ResponseStatus(HttpStatus.OK)
  @PostMapping("/post")
  public void post(@RequestBody PostCreate postCreate) {
    PostWrite postWrite = PostWrite.from(postCreate);
    postService.write(postWrite);
  }

}
