package org.example.postory.domain.post.repository;

import org.example.postory.domain.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

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


}
