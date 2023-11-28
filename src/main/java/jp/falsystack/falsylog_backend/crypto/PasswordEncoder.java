package jp.falsystack.falsylog_backend.crypto;

public interface PasswordEncoder {

  String encrypt(String rawPassword);

  boolean matches(String rawPassword, String encryptedPassword);

}
