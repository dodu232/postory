package org.example.postory.domain.user.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.postory.domain.auth.JwtProvider;
import org.example.postory.domain.user.dto.SignupRequestDto;
import org.example.postory.domain.user.dto.SignupResponseDto;
import org.example.postory.domain.user.dto.UserRequestDto;
import org.example.postory.domain.user.dto.UserResponseDto;
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
    public ResponseEntity<String> test(@AuthenticationPrincipal UserDetails userDetails){
        return ResponseEntity.status(HttpStatus.OK).body(userDetails.getUsername());
  
    @GetMapping("/profile/{UserId}")
    public ResponseEntity<UserProfileResponseDto> getUserInfo(
        @PathVariable Long UserId,
        @AuthenticationPrincipal Long authUserId
    ){
        return new ResponseEntity<>(userService.getProfile(authUserId, UserId), HttpStatus.OK);
    }

    @PatchMapping("/profile")
    public ResponseEntity<UserResponseDto.PatchProfile> updateProfile(
        @AuthenticationPrincipal Long authUserId,
        @RequestBody UserRequestDto.PatchProfile profile
    ){
        return ResponseEntity.status(HttpStatus.OK).body(userService.updateProfile(authUserId, profile));
    }
}

