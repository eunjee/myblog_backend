package jp.falsystack.falsylog_backend.config;

import java.io.Serializable;
import jp.falsystack.falsylog_backend.exception.PostNotFound;
import jp.falsystack.falsylog_backend.repository.post.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;

@Slf4j
@RequiredArgsConstructor
public class MyblogPermissionEvaluator implements PermissionEvaluator {

  private final PostRepository postRepository;

  @Override
  public boolean hasPermission(Authentication authentication, Object targetDomainObject,
      Object permission) {
    return false;
  }

  @Override
  public boolean hasPermission(Authentication authentication, Serializable targetId,
      String targetType, Object permission) {
    var principal = (UserPrincipal) authentication.getPrincipal();

    var post = postRepository.findById((Long) targetId)
        .orElseThrow(PostNotFound::new);

    if (!post.getMemberId().equals(principal.getUserId())) {
      log.error("[인가 실패] 해당 사용자가 작성한 글이 아닙니다. targetId = {}, userId = {}", targetId,
          principal.getUserId());
      return false;
    }

    return true;
  }
}
