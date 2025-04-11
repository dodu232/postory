package org.example.postory.domain.user.repository;

import org.example.postory.domain.user.entity.Following;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface FollowingRepository extends JpaRepository<Following, Long> {

    //팔로잉 수 반환
    Long countByUser_id(Long userId);

    //팔로워 수 반환
    Long countByFollowingUser_id(Long followingUserId);

    boolean existsByUserIdAndFollowingUserId(Long userId, Long followingUserId);

    Optional<Integer> deleteByUserIdAndFollowingUserId(Long userId, Long followingUserId);

    @Query("""
        SELECT f FROM Following f
        WHERE f.user.id = :userId
            AND f.id < :cursorId
        ORDER BY f.id DESC
    """)
    List<Following> findFollowingsByCursor(Long userId, Long cursorId, Pageable pageable);

    @Query("""
        SELECT f FROM Following f
        WHERE f.followingUser.id = :userId
            AND f.id < :cursorId
        ORDER BY f.id DESC
    """)
    List<Following> findFollowersByCursor(Long userId, Long cursorId, Pageable pageable);

    void deleteAllByUser_Id(Long authUserId);

    void deleteAllByFollowingUser_Id(Long authUserId);
}
