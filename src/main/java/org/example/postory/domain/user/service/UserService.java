package org.example.postory.domain.user.service;


import org.example.postory.domain.user.dto.SignupRequestDto;
import org.example.postory.domain.user.dto.SignupResponseDto;
import org.example.postory.domain.user.dto.UserProfileResponseDto;
import lombok.RequiredArgsConstructor;
import org.example.postory.domain.user.dto.UserRequestDto;
import org.example.postory.domain.user.dto.UserRequestDto.PatchProfile;
import org.example.postory.domain.user.dto.UserResponseDto;
import org.example.postory.domain.user.entity.User;
import org.example.postory.domain.user.repository.UserRepository;
import org.example.postory.global.error.ApiException;
import org.example.postory.global.error.response.ErrorType;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

public interface UserService {
    SignupResponseDto signup(SignupRequestDto requestDto);

    String getRefreshToken(long id);

    void saveToken(long id, String refreshToken);

    User getByEmail(String email);
  
    UserProfileResponseDto getProfile(Long authUserId, Long UserId);

    UserResponseDto.PatchProfile updateProfile(Long authUserId, PatchProfile profile);
}
