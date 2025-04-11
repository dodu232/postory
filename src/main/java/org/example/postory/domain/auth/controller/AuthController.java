package org.example.postory.domain.auth.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.postory.domain.auth.dto.AuthRequestDto;
import org.example.postory.domain.auth.jwt.JwtToken;
import org.example.postory.domain.auth.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService service;

    /**
     * 로그인
     */
    @PostMapping("/login")
    public ResponseEntity<JwtToken> login(@Valid @RequestBody AuthRequestDto.Login dto
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(service.login(dto));
    }

    /**
     * 로그아웃
     */
    @PostMapping("/logout")
    public ResponseEntity<Void> login(@AuthenticationPrincipal UserDetails userDetails) {
        service.logout(userDetails);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * 액세스 토큰 재발급 요청
     */
    @PostMapping("/reissue")
    public ResponseEntity<JwtToken> reIssue(@RequestHeader("Authorization") String bearerToken) {
        return ResponseEntity.status(HttpStatus.OK).body(service.reIssue(bearerToken));

    }
}
