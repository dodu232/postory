package org.example.postory.domain.user.service;

import org.example.postory.domain.user.dto.UserResponseDto;

public class UserServiceImpl implements UserService{

    //유저 레포지토리
    //팔로우

    @Override
    public UserResponseDto getProfile(Long loginUserId, Long UserId) {

        //로그인 한 유저가 자신의 프로필에 들어왔을 경우
        if(loginUserId.equals(UserId)){

            //팔로우, 팔로잉 상태 보여주지 않는다.
            // 이름, 상태메시지, 팔로우/팔로워 수, 게시글 목록

        }else{ // 다른 사람의 프로필에 들어갔을 경우

            //해당 사용자의 프로필이 퍼블릭 상태인지 확인
            //팔로우상태인지 확인

            // 1. 언팔로우 + 프라이빗 계정
            // 볼 수 없다 메시지  + ( 200 ok ) 리턴

            // 2. 그 외( 언팔로우 + 퍼블릭 / 팔로우 + 퍼블릭,프라이빗 )
            //해당 사용자의 이름, 상태메시지, 팔로우 여부, 팔로우/팔로워 수, 게시글 목록
        }
        return null;
    }
}
