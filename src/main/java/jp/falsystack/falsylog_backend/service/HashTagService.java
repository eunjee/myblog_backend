package jp.falsystack.falsylog_backend.service;

import java.util.List;
import jp.falsystack.falsylog_backend.exception.TagNotFound;
import jp.falsystack.falsylog_backend.repository.HashTagRepository;
import jp.falsystack.falsylog_backend.response.TagPostResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class HashTagService {

  private final HashTagRepository hashTagRepository;

  public List<TagPostResponse> getPosts(String tagName) {
    var hashTag = hashTagRepository.findByName("#" + tagName)
        .orElseThrow(TagNotFound::new);
    var postHashTags = hashTag.getPostHashTags();

    return postHashTags.stream().map(postHashTag ->
        TagPostResponse.from(postHashTag.getPost())).toList();
  }


}
