package org.example.postory.domain.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class SignupRequestDto {

    @Pattern(
            regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$",
            message = "유효한 이메일 형식이어야 합니다."
    )
    private String email;

    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
            message = "비밀번호는 대소문자, 숫자, 특수문자를 포함한 8자 이상이어야 합니다."
    )
    @Size(max = 20, message = "비밀번호는 20자 이하여야 합니다.")
    @NotBlank(message = "비밀번호는 필수입니다.")
    private String password;

    @Size(max = 20, message = "이름은 20자 이하여야 합니다.")
    @NotBlank(message = "이름은 필수입니다.")
    private String name;

    @Pattern(
            regexp = "^01[016789]-\\d{3,4}-\\d{4}$",
            message = "유효한 휴대폰 번호 형식이어야 합니다. ex) 010-1234-5678"
    )
    @NotBlank(message = "휴대폰 번호는 필수입니다.")
    private String phone;
}
