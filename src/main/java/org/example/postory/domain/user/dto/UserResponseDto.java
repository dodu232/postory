package org.example.postory.domain.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.example.postory.domain.user.entity.User;

public class UserResponseDto {

    @Getter
    @RequiredArgsConstructor
    public static class UpdateProfile {

        private final Long id;
        private final String name;
        private final String introduction;
        private final Boolean gender;
        @JsonProperty("isUserPublic")  // isUserPublic을 UserPublic으로 추론하는 에러에 대한 해결코드
        private final Boolean isUserPublic;

        public UpdateProfile(User user) {
            this.id = user.getId();
            this.name = user.getName();
            this.introduction = user.getIntroduction();
            this.gender = user.isGender();
            this.isUserPublic = user.isUserPublic();
        }
    }
}
