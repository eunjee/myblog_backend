package jp.falsystack.falsylog_backend.service;

import java.util.List;
import jp.falsystack.falsylog_backend.domain.Post;
import jp.falsystack.falsylog_backend.repository.PostRepository;
import jp.falsystack.falsylog_backend.response.PostResponse;
import jp.falsystack.falsylog_backend.service.dto.PostWrite;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostService {

  private final PostRepository postRepository;

  public void write(PostWrite postWrite) {
    var post = Post.from(postWrite);
    postRepository.save(post);
  }

  public List<PostResponse> getPosts() {
    return postRepository.findAll().stream()
        .map(PostResponse::from).toList();
  }
}
