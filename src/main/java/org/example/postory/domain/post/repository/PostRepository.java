package org.example.postory.domain.post.repository;

import java.time.LocalDateTime;
import org.example.postory.domain.post.dto.PostResponseDto.ProfileInquiry;
import org.example.postory.domain.post.entity.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.stream.Collectors;
import org.example.postory.domain.post.dto.PostResponseDto;


import java.util.Optional;

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
    """)
    Optional<Post> findVisiblePost(@Param("id") Long id, @Param("userId") Long userId);

    //공개 게시글 + 삭제되지 않은 게시글 + 수정일 기준 최신순 정렬
    List<Post> getAllByUser_IdAndDeletedAtIsNullAndIsPublicIsTrueOrderByUpdatedAt(Long userId);

    default List<ProfileInquiry> getVisiblePostsByUser(Long userId) {
        return getAllByUser_IdAndDeletedAtIsNullAndIsPublicIsTrueOrderByUpdatedAt(userId)
            .stream().map(PostResponseDto.ProfileInquiry::new).collect(Collectors.toList());
    }


    List<Post> getAllByUser_IdAndDeletedAtIsNullOrderByUpdatedAt(Long userId);

    // 삭제되지 않은 게시글 + 수정일 기준 최신 정렬 ( 함수이름 가독성이 좋지않아서 따로 함더감쌌음)
    default List<ProfileInquiry> getAllMyPosts(Long userId){
        return getAllByUser_IdAndDeletedAtIsNullOrderByUpdatedAt(userId)
            .stream().map(PostResponseDto.ProfileInquiry::new).collect(Collectors.toList());
    }

    @Query("""
        SELECT p FROM Post p
        WHERE ((p.updatedAt < :cursorUpdatedAt)
        OR (p.updatedAt = :cursorUpdatedAt AND p.id < :cursorId))
        AND p.isPublic = true
        ORDER BY p.updatedAt DESC, p.id DESC
    """)
    List<Post> getNewsFeed(
        @Param("cursorUpdatedAt") LocalDateTime cursorUpdatedAt,
        @Param("cursorId") Long cursorId,
        Pageable pageable
    );
}