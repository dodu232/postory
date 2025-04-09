package org.example.postory.domain.user.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.postory.domain.auth.JwtProvider;
import org.example.postory.domain.user.dto.SignupRequestDto;
import org.example.postory.domain.user.dto.SignupResponseDto;
import org.example.postory.domain.user.dto.UserRequestDto;
import org.example.postory.domain.user.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.example.postory.domain.user.dto.UserProfileResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final JwtProvider jwtProvider;

    @PostMapping("/signup")
    public SignupResponseDto signup(@RequestBody @Valid SignupRequestDto requestDto) {
        return userService.signup(requestDto);
    }
  
    @GetMapping
    public ResponseEntity<String> test(){
        return ResponseEntity.status(HttpStatus.OK).body("Test success");
    }
  
    @GetMapping("/profile/{UserId}")
    public ResponseEntity<UserProfileResponseDto> getUserInfo(
        @PathVariable Long UserId,
        @AuthenticationPrincipal Long userId
    ){
        //jwt 필터를 확인해보니 resolveToken 함수안에서 token 스트링값을 찾는 방법을 볼 수 있음.
        // .substring(7); << 한게 token 스트링값이고, jwtProvider 클래스 안에 extractUserId 함수가 로그인한 유저 아이디값 돌려줌
        //String token = authorizationHeader.substring(7);
        //Long authUserId = jwtProvider.extractUserId(token);
        return new ResponseEntity<>(userService.getProfile(userId, UserId), HttpStatus.OK);
    }

    @PatchMapping("/profile")
    public ResponseEntity<UserProfileResponseDto> updateProfile(
        @RequestHeader("Authorization") String authorizationHeader,
        @RequestBody UserRequestDto.patchProfile profile
    ){
        Long authUserId = jwtProvider.extractUserId(authorizationHeader.substring(7));

        return ResponseEntity.status(HttpStatus.OK).body(userService.updateProfile(authUserId, profile));
    }
}

