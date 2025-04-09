package org.example.postory.domain.user.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.example.postory.domain.user.entity.User;

public class UserResponseDto {


    @RequiredArgsConstructor
    public static class PatchProfile{
        private final Long id;
        private final String name;
        private final String introduction;
        private final Boolean gender;
        private final Boolean isPublic;

        public PatchProfile(User user){
            this.id = user.getId();
            this.name = user.getName();
            this.introduction = user.getIntroduction();
            this.gender = user.isGender();
            this.isPublic = user.isPublic();
        }
    }
}
