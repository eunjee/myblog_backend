package jp.falsystack.falsylog_backend.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import jp.falsystack.falsylog_backend.domain.Member;
import jp.falsystack.falsylog_backend.repository.MemberRepository;
import jp.falsystack.falsylog_backend.request.Login;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

  @Autowired
  private ObjectMapper objectMapper;
  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private MemberRepository memberRepository;

  @Test
  @DisplayName("ID와 PW를 입력하여 존재하는 사용자의 경우 정상적으로 로그인처리가 된다.")
  void loginSuccess() throws Exception {
    // given
    var member = Member.of(
        "Spring",
        "spring@spring.com",
        "1q2w3e4r",
        LocalDateTime.now()
    );
    memberRepository.save(member);

    var login = Login.of("spring@spring.com", "1q2w3e4r");
    var json = objectMapper.writeValueAsString(login);

    // expected
    mockMvc.perform(post("/auth/login")
            .contentType(APPLICATION_JSON)
            .content(json))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.accessToken", Matchers.notNullValue()))
        .andDo(print());
  }

}