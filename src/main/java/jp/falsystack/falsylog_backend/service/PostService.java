package jp.falsystack.falsylog_backend.service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import jp.falsystack.falsylog_backend.domain.HashTag;
import jp.falsystack.falsylog_backend.domain.Post;
import jp.falsystack.falsylog_backend.domain.PostHashTag;
import jp.falsystack.falsylog_backend.repository.HashTagRepository;
import jp.falsystack.falsylog_backend.repository.PostRepository;
import jp.falsystack.falsylog_backend.response.PostResponse;
import jp.falsystack.falsylog_backend.service.dto.PostWrite;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {

  private final PostRepository postRepository;
  private final HashTagRepository hashTagRepository;

  @Transactional
  public void write(PostWrite postWrite) {
    var post = Post.from(postWrite);

    if (StringUtils.hasText(postWrite.getHashTags())) {
      post.addPostHashTags(postWrite.getHashTags());
    }
    postRepository.save(post);
  }

  public List<PostResponse> getPosts() {
    return postRepository.findAll().stream()
        .map(PostResponse::from).toList();
  }

  @Transactional
  public PostResponse getPost(Long postId) {
    // TODO: Domain全体の例外が決まったら書き換えする。
    var post = postRepository.findById(postId)
        .orElseThrow(() -> new NoSuchElementException("Userがないです。"));

    var list = new ArrayList<HashTag>();
    if (post.getPostHashTags() != null && !post.getPostHashTags().isEmpty()) {
      for (PostHashTag postHashTag : post.getPostHashTags()) {
        log.info("postHashTag = {}", postHashTag.getHashTag().getName());
        list.add(postHashTag.getHashTag());
      }
    }

    var response = PostResponse.from(post);
    response.addHashTags(list);

    return response;
  }

  public void delete(Long postId) {
    // TODO: Domain全体の例外が決まったら書き換えする。
    var post = postRepository.findById(postId)
        .orElseThrow(NoSuchElementException::new);
    postRepository.delete(post);
  }
}
