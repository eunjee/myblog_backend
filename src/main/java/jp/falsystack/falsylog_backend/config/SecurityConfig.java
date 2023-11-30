package jp.falsystack.falsylog_backend.config;

import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;

import jp.falsystack.falsylog_backend.repository.MemberRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

@Configuration
@EnableWebSecurity(debug = true) // debugモードはstgでは必ずoffにすること
public class SecurityConfig {

  @Bean
  MvcRequestMatcher.Builder mvc(HandlerMappingIntrospector introSpector) {
    return new MvcRequestMatcher.Builder(introSpector);
  }

  @Bean
  public WebSecurityCustomizer webSecurityCustomizer(MvcRequestMatcher.Builder mvc) {
    return web -> web.ignoring()
        .requestMatchers(
            mvc.pattern("/favicon.ico"),
            mvc.pattern("/error"),
            mvc.pattern("/h2-console/**")
        )
        .requestMatchers(toH2Console());
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http, MvcRequestMatcher.Builder mvc)
      throws Exception {
    return http
        .authorizeHttpRequests((authorize) ->
            authorize.requestMatchers(
                mvc.pattern("/auth/signup"),
                mvc.pattern("/auth/login")
            ).permitAll().anyRequest().authenticated()
        )
        .formLogin(form -> {
          form.defaultSuccessUrl("/");
          form.loginPage("/auth/login");
          form.loginProcessingUrl("/auth/login");
          form.usernameParameter("username");
          form.passwordParameter("password");
        })
        .csrf(AbstractHttpConfigurer::disable)
        .build();
  }

  @Bean
  public UserDetailsService userDetailsService(MemberRepository memberRepository) {
    return username -> {
      var member = memberRepository.findByEmail(username)
          .orElseThrow(() -> new UsernameNotFoundException(username + "을 찾을수 없습니다."));
      return new UserPrincipal(member);
    };
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new SCryptPasswordEncoder(16, 8, 1, 32, 64);
  }
}
