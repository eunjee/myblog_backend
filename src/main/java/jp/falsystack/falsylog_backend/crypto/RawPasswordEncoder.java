package jp.falsystack.falsylog_backend.crypto;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

// 暗号化しない（テスト目的）
@Profile("test")
@Component
public class RawPasswordEncoder implements PasswordEncoder{

  @Override
  public String encrypt(String rawPassword) {
    return rawPassword;
  }

  @Override
  public boolean matches(String rawPassword, String encryptedPassword) {
    return rawPassword.equals(encryptedPassword);
  }
}
