package org.example.postory.domain.auth.service;

import lombok.RequiredArgsConstructor;
import org.example.postory.domain.auth.JwtProvider;
import org.example.postory.domain.auth.dto.AuthRequestDto;
import org.example.postory.domain.auth.dto.JwtToken;
import org.example.postory.domain.user.entity.User;
import org.example.postory.domain.user.service.UserService;
import org.example.postory.global.exception.ApiException;
import org.example.postory.global.exception.ErrorType;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;
    private final JwtProvider jwtProvider;

    public JwtToken login(AuthRequestDto.Login dto){
        User user = userService.getByEmail(dto.getEmail());
        if (!user.getPassword().equals(dto.getPassword())) {
            throw new ApiException(HttpStatus.UNAUTHORIZED, ErrorType.INVALID_PARAMETER,
                "비밀번호 불일치");
        }
        return jwtProvider.generateToken(user.getId());
    }
}
