package org.example.postory.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.example.postory.domain.user.dto.SignupRequestDto;
import org.example.postory.domain.user.dto.SignupResponseDto;
import org.example.postory.domain.user.entity.User;
import org.example.postory.domain.user.repository.UserRepository;
import org.example.postory.global.error.ApiException;
import org.example.postory.global.util.PasswordEncoder;
import org.springframework.stereotype.Service;

import static org.example.postory.global.error.response.ErrorType.*;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public SignupResponseDto signup(SignupRequestDto requestDto) {
        if (userRepository.existsByEmail(requestDto.getEmail())) {
            throw new ApiException(DUPLICATE_EMAIL);
        }

        if (userRepository.existsByPhone(requestDto.getPhone())) {
            throw new ApiException(DUPLICATE_PHONE);
        }

        User user = User.builder()
                .email(requestDto.getEmail())
                .password(PasswordEncoder.encode(requestDto.getPassword()))
                .phone(requestDto.getPhone())
                .build();

        User savedUser = userRepository.save(user);
        return new SignupResponseDto(savedUser.getId());
    }
}
