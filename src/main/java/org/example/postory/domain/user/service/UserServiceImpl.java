package org.example.postory.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.example.postory.domain.user.dto.SignupRequestDto;
import org.example.postory.domain.user.dto.SignupResponseDto;
import org.example.postory.global.util.PasswordEncoder;
import static org.example.postory.global.error.response.ErrorType.*;
import org.example.postory.domain.user.entity.User;
import org.example.postory.domain.user.repository.UserRepository;
import org.example.postory.global.error.ApiException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository repository;
    
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

    /**
     * refreshToken 가져오기
     */
    public String getRefreshToken(long id) {
        User findUser = repository.findById(id)
            .orElseThrow(() -> new ApiException(USER_NOT_FOUND));
        return findUser.getRefreshToken();
    }

    /**
     * refreshToken 저장
     */
    @Transactional
    public void saveToken(long id, String refreshToken) {
        User findUser = repository.findById(id)
            .orElseThrow(() -> new ApiException(USER_NOT_FOUND));
        findUser.updateToken(refreshToken);
    }

    public User getByEmail(String email) {
        return repository.findByEmail(email)
            .orElseThrow(() -> new ApiException(EMAIL_NOT_FOUND));
    }
}
