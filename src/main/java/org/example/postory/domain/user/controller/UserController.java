package org.example.postory.domain.user.controller;

import org.example.postory.domain.user.dto.*;
import org.example.postory.global.common.pagination.CursorResponseDto;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.postory.domain.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * 회원가입
     */
    @PostMapping("/signup")
    public SignupResponseDto signup(@RequestBody @Valid SignupRequestDto requestDto) {
        return userService.signup(requestDto);
    }

    @GetMapping
    public ResponseEntity<String> test(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.status(HttpStatus.OK).body(userDetails.getUsername());
    }

    /**
     * 프로필조회
     */
    @GetMapping("/profile/{UserId}")
    public ResponseEntity<UserProfileResponseDto> getUserInfo(
        @PathVariable Long UserId,
        @AuthenticationPrincipal UserDetails userDetails
    ) {
        return ResponseEntity.status(HttpStatus.OK)
            .body(userService.getProfile(Long.parseLong(userDetails.getUsername()), UserId));
    }

    /**
     * 프로필 수정
     */
    @PatchMapping("/profile")
    public ResponseEntity<UserResponseDto.UpdateProfile> updateProfile(
        @AuthenticationPrincipal UserDetails userDetails,
        @RequestBody @Valid UserRequestDto.UpdateProfile profile
    ) {
        return ResponseEntity.status(HttpStatus.OK)
            .body(userService.updateProfile(Long.parseLong(userDetails.getUsername()), profile));
    }

    /**
     * 팔로우
     */
    @PostMapping("/follow/{followingId}")
    public ResponseEntity<String> follow(
            @AuthenticationPrincipal UserDetails userDetail,
            @PathVariable Long followingId
    ) {
        userService.follow(Long.parseLong(userDetail.getUsername()), followingId);
        return ResponseEntity.status(HttpStatus.OK).body("팔로우 성공");
    }

    /**
     * 언팔로우
     */
    @PostMapping("/unfollow/{followingId}")
    public ResponseEntity<String> unfollow(
            @AuthenticationPrincipal UserDetails userDetail,
            @PathVariable Long followingId
    ) {
        userService.unfollow(Long.parseLong(userDetail.getUsername()), followingId);
        return ResponseEntity.status(HttpStatus.OK).body("언팔로우 성공");
    }

    /**
     * 팔로잉 목록 조회
     */
    @GetMapping("/following/{userId}")
    public ResponseEntity<CursorResponseDto<FollowingResponseDto>> getFollowing(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long userId,
            @RequestParam(required = false) Long cursorId,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(userService.getFollowing(Long.parseLong(userDetails.getUsername()), userId, cursorId, size));
    }

    /**
     * 팔로워 목록조회
     */
    @GetMapping("/followers/{userId}")
    public ResponseEntity<CursorResponseDto<FollowingResponseDto>> getFollowers(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long userId,
            @RequestParam(required = false) Long cursorId,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(userService.getFollowers(Long.parseLong(userDetails.getUsername()), userId, cursorId, size));
    }

    /**
     * 계정 비활성화
     */
    @DeleteMapping("/deactivate")
    public ResponseEntity<String> deactivateUser(
        @AuthenticationPrincipal UserDetails userDetails
    ){
        userService.deactivateUser(Long.parseLong(userDetails.getUsername()));
        return ResponseEntity.status(HttpStatus.OK).body("계정이 비활성화됩니다.");
    }
}

