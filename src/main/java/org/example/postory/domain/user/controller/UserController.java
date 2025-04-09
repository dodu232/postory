package org.example.postory.domain.user.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.postory.domain.user.dto.SignupRequestDto;
import org.example.postory.domain.user.dto.SignupResponseDto;
import org.example.postory.domain.user.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
    public ResponseEntity<String> test(){
        return ResponseEntity.status(HttpStatus.OK).body("Test success");
    }
}
