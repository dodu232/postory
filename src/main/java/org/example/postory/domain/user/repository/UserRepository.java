package org.example.postory.domain.user.repository;

import static org.example.postory.global.error.response.ErrorType.USER_NOT_FOUND;

import java.util.Optional;

import org.example.postory.domain.user.entity.User;
import org.example.postory.global.error.ApiException;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    default User findByUserIdOrElseThrow(Long userId){
        return findById(userId).orElseThrow(()-> new ApiException(USER_NOT_FOUND));
    }

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByPhone(String phone);

}
