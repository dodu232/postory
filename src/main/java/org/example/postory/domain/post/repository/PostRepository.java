package org.example.postory.domain.post.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.example.postory.domain.post.dto.PostResponseDto;
import org.example.postory.domain.post.entity.Post;
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

    // 해시태그 검색
    @Query("""
             SELECT new org.example.postory.domain.post.dto.PostResponseDto$SearchList(
                 p.id, p.title, u.name, p.updatedAt
             )
             FROM Post p
             JOIN User u ON p.user.id = u.id
             WHERE (
                (p.updatedAt < :cursorUpdatedAt)
                OR (p.updatedAt = :cursorUpdatedAt AND p.id < :cursorId)
             )
             AND p.hashtag LIKE CONCAT('%', :hashTag, '%')
             AND p.postLikeCount >= :likeMinimum
             AND p.deletedAt IS NULL
             AND p.isPublic = true
        """)
    List<PostResponseDto.SearchList> findByHashTag(
        @Param("hashTag") String hashTag,
        @Param("likeMinimum") int likeMinimum,
        @Param("cursorUpdatedAt") LocalDateTime cursorUpdatedAt,
        @Param("cursorId") Long cursorId,
        Pageable pageable
    );

    // @검색
    @Query("""
             SELECT new org.example.postory.domain.post.dto.PostResponseDto$SearchList(
                 p.id, p.title, u.name, p.updatedAt
             )
             FROM Post p
             JOIN User u ON p.user.id = u.id
             WHERE (
                (p.updatedAt < :cursorUpdatedAt)
                OR (p.updatedAt = :cursorUpdatedAt AND p.id < :cursorId)
             )
             AND u.name LIKE CONCAT('%', :name, '%')
             AND p.postLikeCount >= :likeMinimum
             AND p.deletedAt IS NULL
             AND p.isPublic = true
        """)
    List<PostResponseDto.SearchList> findByMention(
        @Param("name") String name,
        @Param("likeMinimum") int likeMinimum,
        @Param("cursorUpdatedAt") LocalDateTime cursorUpdatedAt,
        @Param("cursorId") Long cursorId,
        Pageable pageable
    );

    // 기본 검색
    @Query("""
             SELECT new org.example.postory.domain.post.dto.PostResponseDto$SearchList(
                 p.id, p.title, u.name, p.updatedAt
             )
             FROM Post p
             JOIN User u ON p.user.id = u.id
             WHERE (
                 (p.updatedAt < :cursorUpdatedAt)
                 OR (p.updatedAt = :cursorUpdatedAt AND p.id < :cursorId)
             )
             AND (p.title LIKE CONCAT('%', :keyword, '%') OR  p.content LIKE CONCAT('%', :keyword, '%'))
             AND p.postLikeCount >= :likeMinimum
             AND p.deletedAt IS NULL
             AND p.isPublic = true
        """)
    List<PostResponseDto.SearchList> findByKeyword(
        @Param("keyword") String keyword,
        @Param("likeMinimum") int likeMinimum,
        @Param("cursorUpdatedAt") LocalDateTime cursorUpdatedAt,
        @Param("cursorId") Long cursorId,
        Pageable pageable
    );
}
