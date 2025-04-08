package org.example.postory.domain.user.service;

import org.example.postory.domain.user.dto.SignupRequestDto;
import org.example.postory.domain.user.dto.SignupResponseDto;

public interface UserService {
    SignupResponseDto signup(SignupRequestDto requestDto);
}
