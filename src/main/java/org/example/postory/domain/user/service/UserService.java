package org.example.postory.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.example.postory.domain.user.entity.User;
import org.example.postory.domain.user.repository.UserRepository;
import org.example.postory.global.exception.ApiException;
import org.example.postory.global.exception.ErrorType;
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
            .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, ErrorType.INVALID_PARAMETER, "유저 못 찾음"));
        return findUser.getRefreshToken();
    }

    /**
     * refreshToken 저장
     */
    @Transactional
    public void saveToken(long id, String refreshToken){
        User findUser = repository.findById(id)
            .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, ErrorType.INVALID_PARAMETER, "유저 못 찾음"));
        findUser.updateToken(refreshToken);
    }

    public User getByEmail(String email){
        return repository.findByEmail(email)
            .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, ErrorType.INVALID_PARAMETER,
                "해당 Email의 유저가 없습니다."));
    }
}
