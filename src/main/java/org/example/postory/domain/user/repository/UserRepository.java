package org.example.postory.domain.user.repository;

import static org.example.postory.global.error.response.ErrorType.USER_NOT_FOUND;

import org.example.postory.domain.user.dto.UserProfileResponseDto;
import org.example.postory.domain.user.dto.UserRequestDto.PatchProfile;
import org.example.postory.domain.user.dto.UserResponseDto;
import org.example.postory.domain.user.entity.User;
import org.example.postory.global.error.ApiException;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import org.springframework.stereotype.Repository;

public interface UserRepository extends JpaRepository<User, Long> {
    default User findByUserIdOrElseThrow(Long userId){
        return findById(userId).orElseThrow(()-> new ApiException(USER_NOT_FOUND));
    }

    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);

}
