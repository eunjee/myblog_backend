package jp.falsystack.falsylog_backend.repository;

import java.util.Optional;
import jp.falsystack.falsylog_backend.domain.Session;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SessionRepository extends JpaRepository<Session, Long> {

  Optional<Session> findByAccessToken(String accessToken);
}
