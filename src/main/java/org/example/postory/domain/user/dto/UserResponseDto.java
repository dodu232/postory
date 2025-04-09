package org.example.postory.domain.user.dto;

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
        private final Boolean isPublic;

        public UpdateProfile(User user) {
            this.id = user.getId();
            this.name = user.getName();
            this.introduction = user.getIntroduction();
            this.gender = user.isGender();
            this.isPublic = user.isPublic();
        }
    }
}
