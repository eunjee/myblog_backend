package jp.falsystack.falsylog_backend.service;

import jp.falsystack.falsylog_backend.exception.InvalidSigninInformation;
import jp.falsystack.falsylog_backend.repository.MemberRepository;
import jp.falsystack.falsylog_backend.request.Login;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

  private final MemberRepository memberRepository;

  @Transactional
  public String signin(Login login) {
    var member = memberRepository.findByEmailAndPassword(login.getEmail(), login.getPassword())
        .orElseThrow(
            InvalidSigninInformation::new);
    var session = member.addSession();
    return session.getAccessToken();
  }

  public Long signinWithJwt(Login login) {
    var member = memberRepository.findByEmailAndPassword(login.getEmail(), login.getPassword())
        .orElseThrow(InvalidSigninInformation::new);

    return member.getId();
  }

}
