package org.example.postory.domain.comment.repository;

import java.util.Optional;
import org.example.postory.domain.comment.entity.CommentLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {

    Optional<CommentLike> findByCommentIdAndUserId(Long commentId, Long userId);

    void deleteAllByUser_Id(Long authUserId);

    void deleteAllByComment_Id(Long id);
}
