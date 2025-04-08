package org.example.postory.domain.user.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
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
    public ResponseEntity<UserResponseDto> getUserInfo(
        @PathVariable Long UserId,
        HttpServletRequest request
    ){
        HttpSession session = request.getSession(false);

        // session에 저장된 유저정보 조회
        Long loginUserId = session.getAttribute("loginUser").getId();

        return new ResponseEntity<>(userService.getProfile(loginUserId, UserId), HttpStatus.OK);
    }

}
