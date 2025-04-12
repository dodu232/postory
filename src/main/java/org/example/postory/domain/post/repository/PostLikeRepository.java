package org.example.postory.domain.post.repository;

import org.example.postory.domain.post.entity.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
    Optional<PostLike> findByPostIdAndUserId(Long postId, Long userId);

    void deleteAllByPost_Id(Long id);

    void deleteAllByUser_Id(Long authUserId);
}
