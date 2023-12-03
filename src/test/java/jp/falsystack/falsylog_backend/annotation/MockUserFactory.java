package jp.falsystack.falsylog_backend.annotation;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

public class MockUserFactory implements WithSecurityContextFactory<CustomWithMockUser> {

  @Override
  public SecurityContext createSecurityContext(CustomWithMockUser annotation) {

    var context = SecurityContextHolder.createEmptyContext();
    var token = UsernamePasswordAuthenticationToken
        .unauthenticated(annotation.email(), annotation.password());

    context.setAuthentication(token);

    return context;
  }
}
