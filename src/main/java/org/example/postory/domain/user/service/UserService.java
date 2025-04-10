package org.example.postory.domain.user.service;

import org.example.postory.domain.user.dto.SignupRequestDto;
import org.example.postory.domain.user.dto.SignupResponseDto;
import org.example.postory.domain.user.dto.UserProfileResponseDto;
import org.example.postory.domain.user.dto.UserRequestDto.UpdateProfile;
import org.example.postory.domain.user.dto.UserResponseDto;
import org.example.postory.domain.user.entity.User;

public interface UserService {

    SignupResponseDto signup(SignupRequestDto requestDto);

    String getRefreshToken(long id);

    void saveToken(long id, String refreshToken);

    User getByEmail(String email);

    UserProfileResponseDto getProfile(Long authUserId, Long UserId);

    void follow(Long userId, Long followingId);

    void unfollow(Long userId, Long followingId);

    UserResponseDto.UpdateProfile updateProfile(Long authUserId, UpdateProfile profile);


}
