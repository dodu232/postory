package org.example.postory.domain.user.service;

import org.example.postory.domain.user.dto.UserResponseDto;

public interface UserService {
    UserResponseDto getProfile(Long loginUserId, Long UserId);
}
