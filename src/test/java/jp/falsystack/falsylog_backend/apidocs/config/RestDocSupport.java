package jp.falsystack.falsylog_backend.apidocs.config;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
@ExtendWith(RestDocumentationExtension.class)
public abstract class RestDocSupport {

  protected MockMvc mockMvc;
  protected ObjectMapper objectMapper = new ObjectMapper();

  @BeforeEach
  void setUp(WebApplicationContext webApplicationContext,
      RestDocumentationContextProvider provider) {
    this.mockMvc = MockMvcBuilders
        .webAppContextSetup(webApplicationContext)// 컨트롤러주입
        .apply(documentationConfiguration(provider))
        .build();
  }

  protected abstract Object initController();

}

