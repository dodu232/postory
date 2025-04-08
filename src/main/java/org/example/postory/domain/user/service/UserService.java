package org.example.postory.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.example.postory.domain.user.entity.User;
import org.example.postory.domain.user.repository.UserRepository;
import org.example.postory.global.error.ApiException;
import org.example.postory.global.error.response.ErrorType;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


public interface UserService {

    String getRefreshToken(long id);

    void saveToken(long id, String refreshToken);

    User getByEmail(String email);
}
