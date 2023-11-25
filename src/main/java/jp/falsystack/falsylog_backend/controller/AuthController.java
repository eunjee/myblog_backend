package jp.falsystack.falsylog_backend.controller;

import jakarta.validation.Valid;
import java.time.Duration;
import jp.falsystack.falsylog_backend.request.Login;
import jp.falsystack.falsylog_backend.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {

  private final AuthService authService;

  @PostMapping("/auth/login")
  public ResponseEntity<?> login(@Valid @RequestBody Login login) {
    var accessToken = authService.signin(login);
    var sessionCookie = ResponseCookie.from("SESSION", accessToken)
        .path("/")
        .domain("localhost") // TODO: 서버 환경에 따른 분리 필요
        .httpOnly(true)
        .sameSite("Strict")
        .secure(false)
        .maxAge(Duration.ofDays(30))
        .build();

    return ResponseEntity
        .ok()
        .header(HttpHeaders.SET_COOKIE, sessionCookie.toString())
        .build();
  }

}
