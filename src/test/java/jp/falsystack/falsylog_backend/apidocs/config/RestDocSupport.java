package jp.falsystack.falsylog_backend.apidocs.config;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

import com.fasterxml.jackson.databind.ObjectMapper;
import jp.falsystack.falsylog_backend.controller.PostController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
@ExtendWith(RestDocumentationExtension.class)
public abstract class RestDocSupport {

  protected MockMvc mockMvc;
  protected ObjectMapper objectMapper = new ObjectMapper();

  @BeforeEach
  void setUp(WebApplicationContext context, RestDocumentationContextProvider provider) {
    this.mockMvc = MockMvcBuilders
        .webAppContextSetup(context)// 컨트롤러주입
        .apply(documentationConfiguration(provider))
        .apply(springSecurity())
        .build();
  }


}

