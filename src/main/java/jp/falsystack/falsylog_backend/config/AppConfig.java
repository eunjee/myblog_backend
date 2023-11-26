package jp.falsystack.falsylog_backend.config;

import java.util.Base64;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class AppConfig {

  @Value("${myblog.key}")
  private byte[] key;

  public void setKey(String key) {
    this.key = Base64.getDecoder().decode(key);
  }

}
