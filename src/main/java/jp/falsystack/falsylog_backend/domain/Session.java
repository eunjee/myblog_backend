package jp.falsystack.falsylog_backend.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Session {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Getter
  private String accessToken;

  @Getter
  @ManyToOne(fetch = FetchType.LAZY)
  private Member member;

  @Builder(access = AccessLevel.PRIVATE)
  private Session(Member member) {
    this.member = member;
    this.accessToken = UUID.randomUUID().toString();
  }

  public static Session from(Member member) {
    return Session.builder()
        .member(member)
        .build();
  }
}
