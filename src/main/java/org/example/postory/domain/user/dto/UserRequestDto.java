package org.example.postory.domain.user.dto;

import lombok.Getter;

public class UserRequestDto {

    @Getter
    public static class patchProfile{
        private String name;
        private String introduction;
        private boolean gender;
        private String password;
    }

}
