package org.example.postory.domain.user.entity;



import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;
import org.example.postory.domain.user.dto.UserRequestDto.UpdateProfile;
import org.example.postory.global.common.BaseEntity;
import org.example.postory.global.util.PasswordEncoder;


@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String phone;

    @Column
    private String name;
    @Column
    private boolean gender;
    @Column
    private String introduction;

    @Column(nullable = false)
    private boolean isUserPublic = true;
    @Column
    private String refreshToken;

    @Builder
    public User(String email, String password, String name, String phone) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.phone = phone;
    }

    public User(long id) {
        this.id = id;
    }

    public void updateToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public void markAsDeleted() {  // soft delete 방식 : 삭제된 시간만 기록
        this.setDeletedAt(LocalDateTime.now());
    }

    public void updateProfile(UpdateProfile updateProfile) {

        if (updateProfile.getName() != null) {
            this.name = updateProfile.getName();
        }
        if (updateProfile.getIntroduction() != null) {
            this.introduction = updateProfile.getIntroduction();
        }
        if (updateProfile.getGender() != null) {
            this.gender = updateProfile.getGender();
        }

        if (updateProfile.getPassword() != null && !PasswordEncoder.matches(updateProfile.getPassword(),
                this.getPassword())) {
            this.password = PasswordEncoder.encode(updateProfile.getPassword());
        }

        if (updateProfile.getIsUserPublic() != null) {
            this.isUserPublic = updateProfile.getIsUserPublic();
        }
    }

}
