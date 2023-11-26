package jp.falsystack.falsylog_backend.config;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jp.falsystack.falsylog_backend.config.data.JwtToken;
import jp.falsystack.falsylog_backend.exception.Unauthorized;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@RequiredArgsConstructor
public class JwtResolver implements HandlerMethodArgumentResolver {

  private final AppConfig appConfig;

  @Override
  public boolean supportsParameter(MethodParameter parameter) {
    return parameter.getParameterType().equals(JwtToken.class);
  }

  @Override
  public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
      NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

    var jws = webRequest.getHeader(HttpHeaders.AUTHORIZATION);
    if (!StringUtils.hasText(jws)) {
      throw new Unauthorized();
    }

    var secretKey = Keys.hmacShaKeyFor(appConfig.getKey());

    try {
      var claimsJws = Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(jws);
      return JwtToken.from(Long.parseLong(claimsJws.getPayload().getSubject()));
    } catch (JwtException e) {
      throw new Unauthorized();
    }
  }
}
