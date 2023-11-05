package jp.falsystack.falsylog_backend.controller;

import java.util.List;
import jp.falsystack.falsylog_backend.response.TagPostResponse;
import jp.falsystack.falsylog_backend.service.HashTagService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/tags")
@RequiredArgsConstructor
public class HashTagController {

  private final HashTagService hashTagService;

  @GetMapping("/{tagName}")
  public List<TagPostResponse> getPosts(@PathVariable String tagName) {
    return hashTagService.getPosts(tagName);
  }
}
