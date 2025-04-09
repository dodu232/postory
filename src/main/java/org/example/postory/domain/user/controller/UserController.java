package org.example.postory.domain.user.controller;

import org.example.postory.domain.user.dto.UserRequestDto;
import org.example.postory.domain.user.dto.UserResponseDto;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        @PathVariable Long UserId,
        @AuthenticationPrincipal UserDetails userDetails
    ){
        return new ResponseEntity<>(userService.getProfile(Long.parseLong(userDetails.getUsername()), UserId), HttpStatus.OK);
    }

    @PatchMapping("/profile")
    public ResponseEntity<UserResponseDto.PatchProfile> updateProfile(
        @AuthenticationPrincipal UserDetails userDetails,
        @RequestBody @Valid UserRequestDto.PatchProfile profile
    ){
        return new ResponseEntity<>(userService.updateProfile(Long.parseLong(userDetails.getUsername()), profile),
                                HttpStatus.OK);
    }
}

