package org.example.postory.domain.user.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;

public class UserRequestDto {

    @Getter
    public static class UpdateProfile {

        private String name;

        @Size(max = 200, message = "소개글은 200자 이하로 작성이 가능합니다.")
        private String introduction;

        private Boolean gender;

        @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
            message = "비밀번호는 대소문자, 숫자, 특수문자를 포함한 8자 이상이어야 합니다."
        )
        @Size(max = 20, message = "비밀번호는 최대 20글자까지만 가능합니다.")
        private String password;

        private Boolean isPublic;

    }

}
