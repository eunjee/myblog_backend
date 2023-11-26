package jp.falsystack.falsylog_backend.repository;

import java.util.Optional;
import jp.falsystack.falsylog_backend.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

  Optional<Member> findByEmailAndPassword(String email, String password);
  Optional<Member> findByEmail(String email);

}
