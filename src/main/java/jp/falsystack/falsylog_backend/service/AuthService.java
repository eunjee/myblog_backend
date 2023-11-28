package jp.falsystack.falsylog_backend.service;

import jp.falsystack.falsylog_backend.crypto.PasswordEncoder;
import jp.falsystack.falsylog_backend.domain.Member;
import jp.falsystack.falsylog_backend.exception.AlreadyExistsEmail;
import jp.falsystack.falsylog_backend.exception.InvalidSigninInformation;
import jp.falsystack.falsylog_backend.repository.MemberRepository;
import jp.falsystack.falsylog_backend.request.Login;
import jp.falsystack.falsylog_backend.request.Signup;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

  private final MemberRepository memberRepository;
  private final PasswordEncoder encoder;

  @Transactional
  public String signin(Login login) {
    var member = memberRepository.findByEmail(login.getEmail())
        .orElseThrow(InvalidSigninInformation::new);

    if (!encoder.matches(login.getPassword(), member.getPassword())) {
      throw new InvalidSigninInformation();
    }

    var session = member.addSession();
    return session.getAccessToken();
  }

  public Long signinWithJwt(Login login) {
    var member = memberRepository.findByEmailAndPassword(login.getEmail(), login.getPassword())
        .orElseThrow(InvalidSigninInformation::new);

    return member.getId();
  }

  public void signup(Signup signup) {
    memberRepository.findByEmail(signup.getEmail()).ifPresent((m) -> {
      throw new AlreadyExistsEmail();
    });

    var encryptedPassword = encoder.encrypt(signup.getPassword());

    var member = Member.builder().name(signup.getName()).email(signup.getEmail())
        .password(encryptedPassword).build();

    memberRepository.save(member);
  }
}
