package jp.falsystack.falsylog_backend.service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import jp.falsystack.falsylog_backend.domain.HashTag;
import jp.falsystack.falsylog_backend.domain.Post;
import jp.falsystack.falsylog_backend.domain.PostHashTag;
import jp.falsystack.falsylog_backend.exception.MemberNotFound;
import jp.falsystack.falsylog_backend.exception.PostNotFound;
import jp.falsystack.falsylog_backend.repository.HashTagRepository;
import jp.falsystack.falsylog_backend.repository.MemberRepository;
import jp.falsystack.falsylog_backend.repository.post.PostRepository;
import jp.falsystack.falsylog_backend.request.post.PostSearch;
import jp.falsystack.falsylog_backend.response.PostResponse;
import jp.falsystack.falsylog_backend.service.dto.PostEdit;
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
  private final MemberRepository memberRepository;

  @Transactional
  public void write(PostWrite postWrite) {
    var post = postWrite.toEntity();

    var member = memberRepository.findById(postWrite.getMemberId())
        .orElseThrow(MemberNotFound::new);
    post.addMember(member);

    addPostHashTagToPost(post, postWrite.getHashTags());
    postRepository.save(post);
  }

  @Transactional
  public List<PostResponse> getPosts(PostSearch postSearch) {
    return postRepository.getList(postSearch)
        .stream()
        .map(PostResponse::from)
        .toList();
  }

  @Transactional
  public PostResponse getPost(Long postId) {
    var post = postRepository.findById(postId)
        .orElseThrow(PostNotFound::new);

    var list = new ArrayList<HashTag>();
    if (post.getPostHashTags() != null && !post.getPostHashTags().isEmpty()) {
      for (PostHashTag postHashTag : post.getPostHashTags()) {
        list.add(postHashTag.getHashTag());
      }
    }

    var response = PostResponse.from(post);
    response.addHashTags(list);

    return response;
  }

  public void delete(Long postId) {
    var post = postRepository.findById(postId)
        .orElseThrow(PostNotFound::new);

    postRepository.delete(post);
  }

  @Transactional
  public void update(Long postId, PostEdit postEdit) {
    var post = postRepository.findById(postId)
        .orElseThrow(PostNotFound::new);

    addPostHashTagToPost(post, postEdit.getHashTags());
    post.edit(postEdit);
  }

  private void addPostHashTagToPost(Post post, String hashTags) {
    if (StringUtils.hasText(hashTags)) {
      var postHashTags = new ArrayList<PostHashTag>();

      var pattern = Pattern.compile("#([0-9a-zA-Z가-힣ぁ-んァ-ヶー一-龯ㄱ-ㅎ]*)");
      var matcher = pattern.matcher(hashTags);
      while (matcher.find()) {
        var hashTag = hashTagRepository.findByName(matcher.group())
            .orElse(HashTag.builder().name(matcher.group()).build());

        var postHashTag = PostHashTag.builder()
            .post(post)
            .hashTag(hashTag)
            .build();

        postHashTags.add(postHashTag);
      }
      post.addPostHashTags(postHashTags);
    }
  }
}
