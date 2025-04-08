package org.example.postory.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.example.postory.domain.user.entity.User;
import org.example.postory.domain.user.repository.UserRepository;
import org.example.postory.global.error.ApiException;
import org.example.postory.global.error.response.ErrorType;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;

    /**
     * refreshToken 가져오기
     */
    public String getRefreshToken(long id){
        User findUser = repository.findById(id)
            .orElseThrow(() -> new ApiException(ErrorType.USER_NOT_FOUND));
        return findUser.getRefreshToken();
    }

    /**
     * refreshToken 저장
     */
    @Transactional
    public void saveToken(long id, String refreshToken){
        User findUser = repository.findById(id)
            .orElseThrow(() -> new ApiException(ErrorType.USER_NOT_FOUND));
        findUser.updateToken(refreshToken);
    }

    public User getByEmail(String email){
        return repository.findByEmail(email)
            .orElseThrow(() -> new ApiException(ErrorType.EMAIL_NOT_FOUND));
    }
}
