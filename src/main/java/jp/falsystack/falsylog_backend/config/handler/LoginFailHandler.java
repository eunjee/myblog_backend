package jp.falsystack.falsylog_backend.config.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import jp.falsystack.falsylog_backend.response.ErrorResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

@Slf4j
@RequiredArgsConstructor
public class LoginFailHandler implements AuthenticationFailureHandler {

  private final ObjectMapper objectMapper;

  @Override
  public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
      AuthenticationException exception) throws IOException, ServletException {
    log.error("[인증오류] 아이디 혹은 비밀번호가 올바르지 않습니다.");

    var errorResponse = ErrorResponse.builder()
        .message("아이디 혹은 비밀번호가 올바르지 않습니다.")
        .build();

    response.setHeader("Content-Type", "application/json");
    response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
    response.setStatus(HttpStatus.BAD_REQUEST.value());

    objectMapper.writeValue(response.getWriter(), errorResponse);
  }
}
