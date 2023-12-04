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
import jp.falsystack.falsylog_backend.request.Signup;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String name;
  private String email;
  private String password;
  private LocalDateTime createdAt; // TODO: BaseEntity 의 createdAt와 중복되어서 현재 값이 안들어감 필드명 변경 필요
  @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
  private List<Post> posts = new ArrayList<>();

  // TODO:　ROLE追加必要

  @Builder
  public Member(String name, String email, String password, LocalDateTime createdAt) {
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

  public static Member from(Signup signup) {
    return Member.builder()
        .name(signup.getName())
        .email(signup.getEmail())
        .password(signup.getPassword())
        .createdAt(LocalDateTime.now())
        .build();
  }

}
