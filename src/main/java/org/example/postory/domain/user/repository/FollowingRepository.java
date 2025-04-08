package org.example.postory.domain.user.repository;

import org.example.postory.domain.user.entity.Following;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FollowingRepository extends JpaRepository<Following, Long> {

    //팔로잉 수 반환
    Long countByFollowingUserByUser_id(Long userId);

    //팔로워 수 반환
    Long countByFollowingUserByFollowingUser_id(Long followingUserId);

    boolean existsByFollowingUserIdAndUserId(Long userId, Long followingUserId);
}
