package jp.falsystack.falsylog_backend.config.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

@Slf4j
public class EmailPasswordAuthFilter extends AbstractAuthenticationProcessingFilter {

  private final ObjectMapper objectMapper;

  public EmailPasswordAuthFilter(ObjectMapper objectMapper, String path) {
    super(path);
    this.objectMapper = objectMapper;
  }

  @Override
  public Authentication attemptAuthentication(HttpServletRequest request,
      HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
    log.info("EmailPasswordAuthFilter.attemptAuthentication");
    var login = objectMapper.readValue(request.getInputStream(), Login.class);

    var token = UsernamePasswordAuthenticationToken
        .unauthenticated(login.getEmail(), login.getPassword());

    token.setDetails(this.authenticationDetailsSource.buildDetails(request));
    return this.getAuthenticationManager().authenticate(token);
  }

  @Getter
  private static class Login {
    private String email;
    private String password;
  }
}
