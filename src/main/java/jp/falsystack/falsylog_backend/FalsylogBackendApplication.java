package jp.falsystack.falsylog_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class FalsylogBackendApplication {

  public static void main(String[] args) {
    SpringApplication.run(FalsylogBackendApplication.class, args);
  }

}
