package jp.falsystack.falsylog_backend.controller;

import jp.falsystack.falsylog_backend.config.AppConfig;
import jp.falsystack.falsylog_backend.request.Signup;
import jp.falsystack.falsylog_backend.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {

  private final AuthService authService;

  @PostMapping("/auth/signup")
  public void signup(@RequestBody Signup signup) {
    authService.signup(signup);
  }

}
