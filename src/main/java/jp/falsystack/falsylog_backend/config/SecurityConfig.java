package jp.falsystack.falsylog_backend.config;

import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;

import com.fasterxml.jackson.databind.ObjectMapper;
import jp.falsystack.falsylog_backend.config.filter.EmailPasswordAuthFilter;
import jp.falsystack.falsylog_backend.config.handler.Http401Handler;
import jp.falsystack.falsylog_backend.config.handler.Http403Handler;
import jp.falsystack.falsylog_backend.config.handler.LoginFailHandler;
import jp.falsystack.falsylog_backend.config.handler.LoginSuccessHandler;
import jp.falsystack.falsylog_backend.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.session.security.web.authentication.SpringSessionRememberMeServices;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

@Slf4j
@Configuration
@EnableWebSecurity(debug = true) // debugモードはstgでは必ずoffにすること
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private final ObjectMapper objectMapper;
  private final MemberRepository memberRepository;

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
            authorize
                .anyRequest()
                .permitAll()
        )
        .addFilterBefore(usernamePasswordAuthenticationFilter(),
            UsernamePasswordAuthenticationFilter.class)
        .exceptionHandling(e -> {
          e.accessDeniedHandler(new Http403Handler(objectMapper));
          e.authenticationEntryPoint(new Http401Handler(objectMapper));
        })
        .csrf(AbstractHttpConfigurer::disable)
        .build();
  }

  @Bean
  public EmailPasswordAuthFilter usernamePasswordAuthenticationFilter() {
    var filter = new EmailPasswordAuthFilter(objectMapper, "/auth/login");
    filter.setAuthenticationSuccessHandler(
        new SimpleUrlAuthenticationSuccessHandler("/"));
    filter.setAuthenticationFailureHandler(new LoginFailHandler(objectMapper));
    filter.setAuthenticationSuccessHandler(new LoginSuccessHandler(objectMapper));
    filter.setSecurityContextRepository(
        new HttpSessionSecurityContextRepository());
    filter.setAuthenticationManager(authenticationManager());


    var rememberMeServices = new SpringSessionRememberMeServices();
    rememberMeServices.setAlwaysRemember(true);
    rememberMeServices.setValiditySeconds(3600 * 24 * 30);
    filter.setRememberMeServices(rememberMeServices);

    return filter;
  }

  @Bean
  public AuthenticationManager authenticationManager() {
    var daoProvider = new DaoAuthenticationProvider();
    daoProvider.setPasswordEncoder(passwordEncoder());
    daoProvider.setUserDetailsService(userDetailsService(memberRepository));

    return new ProviderManager(daoProvider);
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
