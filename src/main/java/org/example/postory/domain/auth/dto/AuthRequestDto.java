package org.example.postory.domain.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class AuthRequestDto {

    @Getter
    @AllArgsConstructor
    public static class Login {

        @Email(message = "유효한 이메일 형식이어야 합니다.")
        private String email;

        @Pattern(
                regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
                message = "비밀번호는 대소문자, 숫자, 특수문자를 포함한 8자 이상이어야 합니다."
        )
        @Size(max = 20, message = "비밀번호는 20자 이하여야 합니다.")
        @NotBlank(message = "비밀번호는 필수입니다.")
        private String password;
    }
}
