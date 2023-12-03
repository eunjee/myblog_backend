package jp.falsystack.falsylog_backend.controller;

import jp.falsystack.falsylog_backend.config.UserPrincipal;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {

  @GetMapping("/")
  public String index() {
    return "메인 페이지 입니다.";
  }


  @PreAuthorize("hasRole('USER')")
  @GetMapping("/user")
  public String user(@AuthenticationPrincipal UserPrincipal userPrincipal) {
    return "사용자 페이지 입니다. 🥰";
  }

  @PreAuthorize("hasRole('ADMIN')")
  @GetMapping("/admin")
  public String admin() {
    return "관리자 페이지 입니다.";
  }
}
