package org.example.postory.domain.user.service;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.example.postory.domain.post.dto.PostResponseDto;
import org.example.postory.domain.post.entity.Post;
import org.example.postory.domain.post.repository.PostRepository;
import org.example.postory.domain.user.dto.UserProfileResponseDto;
import org.example.postory.domain.user.entity.User;
import org.example.postory.domain.user.repository.FollowingRepository;
import org.example.postory.domain.user.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    UserRepository userRepository;
    FollowingRepository followingRepository;
    PostRepository postRepository;

    @Transactional(readOnly = true)
    @Override
    public UserProfileResponseDto getProfile(Long loginUserId, Long UserId) {
        //uri에서 받아온 유저값 검증
        User user = userRepository.findByUserIdOrElseThrow(UserId);

        // 다른사람의 프로필 + 팔로우 안했음 + 상대방이 프로필 비공개 상태
        // == 못봄
        if(!loginUserId.equals(UserId)
            && followingRepository.existsByFollowingUserIdAndUserId(loginUserId, UserId)
            && !user.isPublic()){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                "이 사용자는 프로필 비공개 설정 상태이며, 친구가 아닌 경우 정보 열람이 제한됩니다.");
        }

        //팔로잉, 팔로워 수 구하기
        Long followingCnt = followingRepository.countByUser_id(UserId);
        Long followerCnt = followingRepository.countByFollowingUser_id(UserId);

        if(loginUserId.equals(UserId)){ //로그인 한 유저가 자신의 프로필에 들어왔을 경우
            //팔로우, 팔로잉 상태 보여주지 않는다.
            //게시글 가져오기 - 자기자신의 프로필이라 isn't public 한 게시글도 다 불러옴
            List<PostResponseDto> posts = postRepository.getAllMyPosts(UserId);

            return new UserProfileResponseDto(
                user.getId(),
                user.getName(),
                user.getIntroduction(),
                user.isPublic(),
                followingCnt.intValue(),
                followerCnt.intValue(),
                posts
            );
        }else{ // 다른 사람의 프로필에 들어갔을 경우
            //해당 사용자의 이름, 상태메시지, 팔로우 여부, 팔로우/팔로워 수, 게시글 목록
            List<Post> posts = postRepository.getAllPublicPostsByUser_id(UserId);

            return new UserProfileResponseDto(
                user.getId(),
                user.getName(),
                user.getIntroduction(),
                user.isPublic(),
                followingCnt.intValue(),
                followerCnt.intValue(),
                followingRepository.existsByFollowingUserIdAndUserId(loginUserId, UserId),
                posts.stream().map(PostResponseDto::new).collect(Collectors.toList())
            );
        }
    }

}
