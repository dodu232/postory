package org.example.postory.domain.user.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.postory.domain.user.dto.SignupRequestDto;
import org.example.postory.domain.user.dto.SignupResponseDto;
import org.example.postory.domain.user.dto.UserProfileResponseDto;
import org.example.postory.domain.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public SignupResponseDto signup(@RequestBody @Valid SignupRequestDto requestDto) {
        return userService.signup(requestDto);
    }

    @GetMapping
    public ResponseEntity<String> test(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.status(HttpStatus.OK).body(userDetails.getUsername());
    }

    @GetMapping("/profile/{UserId}")
    public ResponseEntity<UserProfileResponseDto> getUserInfo(
        @PathVariable Long UserId
        //(임시로 클래스만 붙임) 토큰정보를 받아오기
        //Authentication authentication
    ) {
        //토큰검증이 필터안에서 이뤄지지 않으면 필터 생성하거나 여기서 토큰 검증절차 필요
        return new ResponseEntity<>(userService.getProfile(3L, UserId), HttpStatus.OK);
    }

    @PostMapping("/follow/{followingId}")
    public ResponseEntity<String> follow(
            @AuthenticationPrincipal UserDetails userDetail,
            @PathVariable Long followingId
    ) {
        userService.follow(Long.parseLong(userDetail.getUsername()), followingId);
        return ResponseEntity.status(HttpStatus.OK).body("팔로우 성공");
    }

    @PostMapping("/unfollow/{followingId}")
    public ResponseEntity<String> unfollow(
            @AuthenticationPrincipal UserDetails userDetail,
            @PathVariable Long followingId
    ) {
        userService.unfollow(Long.parseLong(userDetail.getUsername()), followingId);
        return ResponseEntity.status(HttpStatus.OK).body("언팔로우 성공");
    }
}

