package jp.falsystack.falsylog_backend.repository;

import java.util.Optional;
import jp.falsystack.falsylog_backend.domain.HashTag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HashTagRepository extends JpaRepository<HashTag, Long> {

  Optional<HashTag> findByName(String tagName);
}
