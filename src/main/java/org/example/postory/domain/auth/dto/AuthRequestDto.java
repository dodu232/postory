package org.example.postory.domain.auth.dto;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class AuthRequestDto {

    @Getter
    @AllArgsConstructor
    public static class Login{
        @Email
        private String email;

        private String password;
    }
}
