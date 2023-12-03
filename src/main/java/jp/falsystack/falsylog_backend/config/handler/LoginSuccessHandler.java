package jp.falsystack.falsylog_backend.config.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import jp.falsystack.falsylog_backend.config.UserPrincipal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Slf4j
@RequiredArgsConstructor
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

  private final ObjectMapper objectMapper;

  @Override
  public void onAuthenticationSuccess(
      HttpServletRequest request,
      HttpServletResponse response,
      Authentication authentication) throws IOException, ServletException {

    UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
    log.info("[인증성공] user = {}", principal.getUsername());

    response.setHeader("Content-Type", "application/json");
    response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
    response.setStatus(HttpStatus.OK.value());

    // TODO: login 成功に対するレスポンスが必要かフロントと相談する。
    objectMapper.writeValue(response.getWriter(), null);
  }
}
