package jp.falsystack.falsylog_backend.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Base64;
import java.util.Objects;
import javax.crypto.SecretKey;
import jp.falsystack.falsylog_backend.config.data.UserSession;
import jp.falsystack.falsylog_backend.exception.Unauthorized;
import jp.falsystack.falsylog_backend.repository.SessionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Slf4j
@RequiredArgsConstructor
public class AuthResolver implements HandlerMethodArgumentResolver {

//  private final SessionRepository sessionRepository;

  private final AppConfig appConfig;

  @Override
  public boolean supportsParameter(MethodParameter parameter) {
    return parameter.getParameterType().equals(UserSession.class);
  }

  @Override
  public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
      NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

    var jws = webRequest.getHeader("Authorization");
    if (!StringUtils.hasText(jws)) {
      throw new Unauthorized();
    }

    var secretKey = Keys.hmacShaKeyFor(appConfig.getKey());

    try {
      var claimsJws = Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(jws);
      return UserSession.from(Long.parseLong(claimsJws.getPayload().getSubject()));
    } catch (JwtException e) {
      throw new Unauthorized();
    }

  }

// cookie session
//  @Override
//  public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
//      NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
//    log.info("AuthResolver.resolveArgument");
//    var httpServletRequest = webRequest.getNativeRequest(HttpServletRequest.class);
//    if (Objects.isNull(httpServletRequest)) {
//      throw new Unauthorized();
//    }
//
//    var sessionCookie = Arrays.stream(httpServletRequest.getCookies())
//        .filter(cookie -> cookie.getName().equals("SESSION"))
//        .findAny()
//        .orElseThrow(Unauthorized::new);
//
//    var session = sessionRepository
//        .findByAccessToken(sessionCookie.getValue())
//        .orElseThrow(Unauthorized::new);
//
//    return UserSession.from(session.getMember().getId());
//  }
}
