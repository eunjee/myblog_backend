package jp.falsystack.falsylog_backend;

import jp.falsystack.falsylog_backend.config.AppConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

//@EnableConfigurationProperties(AppConfig.class)
@EnableJpaAuditing
@SpringBootApplication
public class FalsylogBackendApplication {

  public static void main(String[] args) {
    SpringApplication.run(FalsylogBackendApplication.class, args);
  }

}
