package jp.falsystack.falsylog_backend.repository;

import jp.falsystack.falsylog_backend.domain.Resume;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResumeRepository extends JpaRepository<Resume,Long> {
}
