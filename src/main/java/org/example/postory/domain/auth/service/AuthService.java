package org.example.postory.domain.auth.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.postory.domain.auth.dto.AuthRequestDto;
import org.example.postory.domain.auth.jwt.JwtProvider;
import org.example.postory.domain.auth.jwt.JwtToken;
import org.example.postory.domain.user.entity.User;
import org.example.postory.domain.user.repository.UserRepository;
import org.example.postory.global.error.ApiException;
import org.example.postory.global.error.response.ErrorType;
import org.example.postory.global.util.PasswordEncoder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;

    /**
     * 이메일을 통해 유저 객체를 가져옴 비밀번호가 맞는지 확인
     * accessToken과 refreshToken을 반환
     */
    public JwtToken login(AuthRequestDto.Login dto) {
        User findUser = userRepository.findByEmail(dto.getEmail())
            .orElseThrow(() -> new ApiException(ErrorType.USER_NOT_FOUND));
        if (!PasswordEncoder.matches(dto.getPassword(), findUser.getPassword())) {
            throw new ApiException(ErrorType.INVALID_PASSWORD);
        }
        return jwtProvider.generateToken(findUser.getId());
    }

    /**
     * 로그아웃 하면서 DB에 저장된 refreshToken을 삭제함
     */
    @Transactional
    public void logout(UserDetails userDetails) {
        long userId = Long.parseLong(userDetails.getUsername());
        User findUser = userRepository.findById(userId)
            .orElseThrow(() -> new ApiException(ErrorType.USER_NOT_FOUND));
        findUser.updateToken(null);
    }

    /**
     * accessToken을 재발급
     */
    public JwtToken reIssue(String bearerToken) {
        String refreshToken = bearerToken.replace("Bearer ", "");
        return jwtProvider.refreshToken(refreshToken);
    }
}
