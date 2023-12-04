package jp.falsystack.falsylog_backend.service;

import jp.falsystack.falsylog_backend.domain.Comment;
import jp.falsystack.falsylog_backend.exception.CommentNotFound;
import jp.falsystack.falsylog_backend.exception.InvalidPassword;
import jp.falsystack.falsylog_backend.exception.PostNotFound;
import jp.falsystack.falsylog_backend.repository.comment.CommentRepository;
import jp.falsystack.falsylog_backend.repository.post.PostRepository;
import jp.falsystack.falsylog_backend.request.CommentCreate;
import jp.falsystack.falsylog_backend.request.CommentDelete;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {

  private final PostRepository postRepository;
  private final CommentRepository commentRepository;
  private final PasswordEncoder passwordEncoder;

  @Transactional
  public void write(Long postId, CommentCreate request) {
    var post = postRepository.findById(postId).orElseThrow(PostNotFound::new);

    var encryptedPassword = passwordEncoder.encode(request.getPassword());

    var comment = Comment.builder()
        .author(request.getAuthor())
        .password(encryptedPassword)
        .content(request.getContent())
        .build();

    // TODO: web영역의 dto와 service단의 dto를 나눠줘야한다.
    // toEntity등은 web영역이 아니기 때문에 service단의 dto등에 만들어 주는게 좋다.
    comment.addPost(post);
    commentRepository.save(comment);
  }

  public void delete(Long commentId, CommentDelete request) {
    var comment = commentRepository.findById(commentId).orElseThrow(CommentNotFound::new);

    if (!passwordEncoder.matches(request.getPassword(), comment.getPassword())) {
      throw new InvalidPassword();
    }

    commentRepository.delete(comment);
  }
}
