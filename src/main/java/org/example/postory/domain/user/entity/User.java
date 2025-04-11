package org.example.postory.domain.user.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;
import org.example.postory.global.common.BaseEntity;


@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @Column(nullable = false)
    private String email;

    @Setter
    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String phone;

    @Setter
    private String name;
    @Setter
    private boolean gender;
    @Setter
    private String introduction;

    @Setter
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

    public User(long id){
        this.id = id;
    }

    public void updateToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

}
