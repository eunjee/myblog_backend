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
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

@Slf4j
@RequiredArgsConstructor
public class Http403Handler implements AccessDeniedHandler {

  private final ObjectMapper objectMapper;

  @Override
  public void handle(HttpServletRequest request, HttpServletResponse response,
      AccessDeniedException accessDeniedException) throws IOException, ServletException {
    log.error("[인증오류] 403");

    var errorResponse = ErrorResponse.builder()
        .message("접근할 수 없습니다.")
        .build();

    response.setHeader("Content-Type", "application/json");
    response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
    response.setStatus(HttpStatus.FORBIDDEN.value());

    objectMapper.writeValue(response.getWriter(), errorResponse);
  }
}
