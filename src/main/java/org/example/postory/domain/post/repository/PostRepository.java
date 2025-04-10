package org.example.postory.domain.post.repository;

import static org.example.postory.global.error.response.ErrorType.POST_NOT_FOUND;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.example.postory.domain.post.entity.Post;
import org.example.postory.global.error.ApiException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    // 공개 게시물이거나, 비공개 게시물이라면 작성자가 본인인 경우에 게시물 조회 가능
    // :id와 :userId는 메서드 파라미터에서 전달받은 값
    @Query("""
            SELECT p FROM Post p
            WHERE p.id = :id
            AND (
                p.isPublic = true
                OR (:userId IS NOT NULL AND p.user.id = :userId)
            )
            AND p.deletedAt IS NULL
        """)
    Optional<Post> findVisiblePost(@Param("id") Long id, @Param("userId") Long userId);

    //공개 게시글 + 삭제되지 않은 게시글 + 수정일 기준 최신순 정렬
    List<Post> getAllByUser_IdAndDeletedAtIsNullAndIsPublicIsTrueOrderByUpdatedAt(Long userId);

    List<Post> getAllByUser_IdAndDeletedAtIsNullOrderByUpdatedAt(Long userId);


    // 뉴스피드 조회
    @Query("""
            SELECT p FROM Post p
            WHERE ((p.updatedAt < :cursorUpdatedAt)
            OR (p.updatedAt = :cursorUpdatedAt AND p.id < :cursorId))
            AND p.isPublic = true
            AND p.deletedAt IS NULL
            ORDER BY p.updatedAt DESC, p.id DESC
        """)
    List<Post> getNewsFeed(
        @Param("cursorUpdatedAt") LocalDateTime cursorUpdatedAt,
        @Param("cursorId") Long cursorId,
        Pageable pageable
    );

    // 게시물 id로 게시물 조회
    default Post findByIdOrElseThrow(long id) {
        return findById(id).orElseThrow(() -> new ApiException(POST_NOT_FOUND));
    }


}
