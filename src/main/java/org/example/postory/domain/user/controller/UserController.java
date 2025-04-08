package org.example.postory.domain.user.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.example.postory.domain.user.dto.UserProfileResponseDto;
import org.example.postory.domain.user.dto.UserResponseDto;
import org.example.postory.domain.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    UserService userService;

    @GetMapping("/profile/{UserId}")
    public ResponseEntity<UserProfileResponseDto> getUserInfo(
        @PathVariable Long UserId,
        //(임시로 클래스만 붙임) 토큰정보를 받아오기
        Authentication authentication
    ){
        //토큰검증이 필터안에서 이뤄지지 않으면 필터 생성하거나 여기서 토큰 검증절차 필요

        return new ResponseEntity<>(userService.getProfile(authentication.getId(), UserId), HttpStatus.OK);
    }

}
