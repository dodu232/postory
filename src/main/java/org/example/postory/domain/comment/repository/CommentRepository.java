package org.example.postory.domain.comment.repository;

import org.example.postory.domain.comment.entity.Comment;
import org.example.postory.global.error.ApiException;
import org.example.postory.global.error.response.ErrorType;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    // 댓글 전체 조회
    @Query("""
        SELECT c FROM Comment c
        JOIN c.post p
        WHERE p.id = :postId
        AND ((c.updatedAt < :cursorUpdatedAt) OR (c.updatedAt = :cursorUpdatedAt AND c.id < :cursorId))
        AND c.deletedAt IS NULL
        ORDER BY c.updatedAt DESC
        """)
    List<Comment> getComments(
        @Param("cursorUpdatedAt") LocalDateTime cursorUpdatedAt,
        @Param("cursorId") Long cursorId,
        @Param("postId") Long postId,
        Pageable pageable
    );

    default Comment getCommentByIdOrElseThrow(Long commentId) {
        return findById(commentId).orElseThrow(() -> new ApiException(ErrorType.COMMENT_NOT_FOUND));
    }

    List<Comment> getAllByUser_IdAndDeletedAtIsNull(Long userId);

    List<Comment> findAllByPost_Id(Long id);
}
