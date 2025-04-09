package org.example.postory.domain.user.repository;

import org.example.postory.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import java.util.Optional;
import org.springframework.stereotype.Repository;

public interface UserRepository extends JpaRepository<User, Long> {
    default User findByUserIdOrElseThrow(Long userId){
        return findById(userId).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 아이디 값이 존재하지 않습니다. : " + userId));
    }

    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);

}
