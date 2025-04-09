package org.example.postory.domain.user.service;

import org.example.postory.domain.user.dto.SignupRequestDto;
import org.example.postory.domain.user.dto.SignupResponseDto;
import org.example.postory.domain.user.dto.UserProfileResponseDto;
import org.example.postory.domain.user.entity.User;

public interface UserService {
    SignupResponseDto signup(SignupRequestDto requestDto);

    String getRefreshToken(long id);

    void saveToken(long id, String refreshToken);

    User getByEmail(String email);
  
    UserProfileResponseDto getProfile(Long loginUserId, Long UserId);

}
