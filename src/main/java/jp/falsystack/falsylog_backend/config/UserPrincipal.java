package jp.falsystack.falsylog_backend.config;

import java.util.List;
import jp.falsystack.falsylog_backend.domain.Member;
import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

@Getter
public class UserPrincipal extends User {

  private final Long userId;

  public UserPrincipal(Member member) {
    super(member.getEmail(), member.getPassword(), List.of(new SimpleGrantedAuthority("ADMIN")));
    this.userId = member.getId();
  }
}
