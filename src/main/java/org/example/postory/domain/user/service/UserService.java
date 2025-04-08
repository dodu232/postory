package org.example.postory.domain.user.service;

import org.example.postory.domain.user.entity.User;

public interface UserService {

    String getRefreshToken(long id);

    void saveToken(long id, String refreshToken);

    User getByEmail(String email);
}
