package org.example.postory.domain.user.service;

import org.example.postory.domain.user.dto.UserProfileResponseDto;

public interface UserService {
    UserProfileResponseDto getProfile(Long loginUserId, Long UserId);
}
