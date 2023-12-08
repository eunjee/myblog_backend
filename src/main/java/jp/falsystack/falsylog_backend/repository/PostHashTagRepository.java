package jp.falsystack.falsylog_backend.repository;

import jp.falsystack.falsylog_backend.domain.PostHashTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostHashTagRepository extends JpaRepository<PostHashTag, Long> {

}
