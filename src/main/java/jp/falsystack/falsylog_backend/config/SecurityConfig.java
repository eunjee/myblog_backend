package jp.falsystack.falsylog_backend.config;

import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
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
                mvc.pattern("/auth/signin")
            ).permitAll().anyRequest().authenticated()
        )
        .formLogin(form -> {
          form.defaultSuccessUrl("/");
          form.loginPage("/auth/signin");
          form.loginProcessingUrl("/auth/signin");
          form.usernameParameter("username");
          form.passwordParameter("password");
        })
        .userDetailsService(userDetailsService())
        .csrf(AbstractHttpConfigurer::disable)
        .build();
  }

  @Bean
  public UserDetailsService userDetailsService() {
    UserDetails userDetails = User.withDefaultPasswordEncoder()
        .username("falsystack")
        .password("1q2w3e4r")
        .roles("ADMIN")
        .build();

    return new InMemoryUserDetailsManager(userDetails);
  }
}
