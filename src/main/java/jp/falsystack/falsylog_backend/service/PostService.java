package jp.falsystack.falsylog_backend.service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.regex.Pattern;
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
      var postHashTags = new ArrayList<PostHashTag>();
      var pattern = Pattern.compile("#([0-9a-zA-Z가-힣ぁ-んァ-ヶー一-龯]*)");
      var matcher = pattern.matcher(postWrite.getHashTags());

      while (matcher.find()) {
        var hashTag = hashTagRepository.findByName(matcher.group()).orElseThrow(
            () -> new IllegalArgumentException("해시태그가 없습니다.")
        );
        if (hashTag == null) {
          hashTag = HashTag.builder()
              .name(matcher.group())
              .build();
        }

        var postHashTag = PostHashTag.builder()
            .post(post)
            .hashTag(hashTag)
            .build();

        postHashTags.add(postHashTag);

      }
      post.addPostHashTags(postHashTags);
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
