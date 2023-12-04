package jp.falsystack.falsylog_backend.annotation;

import java.util.List;
import jp.falsystack.falsylog_backend.config.UserPrincipal;
import jp.falsystack.falsylog_backend.domain.Member;
import jp.falsystack.falsylog_backend.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

@RequiredArgsConstructor
public class MockUserFactory implements WithSecurityContextFactory<CustomWithMockUser> {

  private final MemberRepository memberRepository;

  @Override
  public SecurityContext createSecurityContext(CustomWithMockUser annotation) {

    var member = Member.builder()
        .email(annotation.email())
        .password(annotation.password())
        .name(annotation.name())
        .build();
    memberRepository.save(member);

    var principal = new UserPrincipal(member);

    var context = SecurityContextHolder.createEmptyContext();
    var authentication = UsernamePasswordAuthenticationToken.authenticated(principal,
        member.getPassword(),
        List.of(new SimpleGrantedAuthority("ROLE_ADMIN")));

    context.setAuthentication(authentication);

    return context;
  }
}
