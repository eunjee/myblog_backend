package jp.falsystack.falsylog_backend.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String name;
  private String email;
  private String password;
  private LocalDateTime createdAt;

  @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
  private final List<Session> sessions = new ArrayList<>();

  @Builder
  private Member(String name, String email, String password, LocalDateTime createdAt) {
    this.name = name;
    this.email = email;
    this.password = password;
    this.createdAt = createdAt;
  }

  public static Member of(String name, String email, String password, LocalDateTime createdAt) {
    return Member.builder()
        .name(name)
        .email(email)
        .password(password)
        .createdAt(createdAt)
        .build();
  }

  public Session addSession() {
    var session = Session.from(this);
    this.sessions.add(session);
    return session;
  }
}
