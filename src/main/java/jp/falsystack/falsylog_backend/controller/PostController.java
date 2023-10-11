package jp.falsystack.falsylog_backend.controller;

import jp.falsystack.falsylog_backend.request.PostCreate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class PostController {

  @ResponseStatus(HttpStatus.OK)
  @PostMapping("/post")
  public void post(@RequestBody PostCreate postCreate) {
    log.info("postCreate = {}", postCreate);
  }

}
