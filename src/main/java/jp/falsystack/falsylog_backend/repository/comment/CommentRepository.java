package jp.falsystack.falsylog_backend.repository.comment;

import jp.falsystack.falsylog_backend.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
