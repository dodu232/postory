package org.example.postory.domain.auth.service;

import lombok.RequiredArgsConstructor;
import org.example.postory.domain.auth.JwtProvider;
import org.example.postory.domain.auth.dto.AuthRequestDto;
import org.example.postory.domain.auth.dto.JwtToken;
import org.example.postory.domain.user.entity.User;
import org.example.postory.domain.user.service.UserService;
import org.example.postory.global.error.ApiException;
import org.example.postory.global.error.response.ErrorType;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;
    private final JwtProvider jwtProvider;

    /**
     * 이메일을 통해 유저 객체를 가져옴
     * 비밀번호가 맞는지 확인(암호화 들어가야 함)
     * accessToken과 refreshToken을 반환
     */
    public JwtToken login(AuthRequestDto.Login dto){
        User user = userService.getByEmail(dto.getEmail());
        if (!user.getPassword().equals(dto.getPassword())) {
            throw new ApiException(ErrorType.INVALID_PASSWORD);
        }
        return jwtProvider.generateToken(user.getId());
    }

    /**
     * accessToken을 재발급
     */
    public JwtToken reIssue(String bearerToken){
        String refreshToken = bearerToken.replace("Bearer ", "");
        return jwtProvider.refreshToken(refreshToken);
    }
}
