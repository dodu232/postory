package org.example.postory.domain.user.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.postory.domain.post.dto.PostResponseDto.NewsFeed;
import org.example.postory.domain.post.repository.PostRepository;
import org.example.postory.domain.user.dto.SignupRequestDto;
import org.example.postory.domain.user.dto.SignupResponseDto;
import org.example.postory.domain.user.dto.UserProfileResponseDto;
import org.example.postory.domain.user.entity.Following;
import org.example.postory.domain.user.entity.User;
import org.example.postory.domain.user.repository.FollowingRepository;
import org.example.postory.domain.user.repository.UserRepository;
import org.example.postory.global.error.ApiException;
import org.example.postory.global.error.response.ErrorType;
import org.example.postory.global.util.PasswordEncoder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import static org.example.postory.global.error.response.ErrorType.*;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final FollowingRepository followingRepository;
    private final PostRepository postRepository;

    /**
     * refreshToken 가져오기
     */
    public String getRefreshToken(long id) {

        User findUser = userRepository.findById(id)
            .orElseThrow(() -> new ApiException(ErrorType.USER_NOT_FOUND));
        return findUser.getRefreshToken();
    }

    /**
     * refreshToken 저장
     */
    @Transactional
    public void saveToken(long id, String refreshToken) {
        User findUser = userRepository.findById(id)
            .orElseThrow(() -> new ApiException(ErrorType.USER_NOT_FOUND));
        findUser.updateToken(refreshToken);
    }

    public User getByEmail(String email) {
        return userRepository.findByEmail(email)
            .orElseThrow(() -> new ApiException(EMAIL_NOT_FOUND));
    }

    @Override
    public SignupResponseDto signup(SignupRequestDto requestDto) {
        if (userRepository.existsByEmail(requestDto.getEmail())) {
            throw new ApiException(DUPLICATE_EMAIL);
        }

        if (userRepository.existsByPhone(requestDto.getPhone())) {
            throw new ApiException(DUPLICATE_PHONE);
        }

        User user = User.builder()
            .email(requestDto.getEmail())
            .password(PasswordEncoder.encode(requestDto.getPassword()))
            .phone(requestDto.getPhone())
            .build();

        User savedUser = userRepository.save(user);
        return new SignupResponseDto(savedUser.getId());
    }

    /**
     * [Service] 프로필 조회 함수 1. controller에서 받아온 유저값 검증 2. 다른사람의 프로필 + 팔로우 안했음 + 상대방이 프로필 비공개 상태 ->
     * 403 3. 자신의 프로필 조회일 경우 - 비공개 개시글 표시o, 팔로잉 여부 표시x 4. 타인의 프로필 조회일 경우 - 비공개 게시글 표시x, 팔로잉 여부 표시o
     *
     * @param loginUserId 현재 로그인 중인 유저 아이디
     * @param UserId      프로필 조회할 유저 아이디
     * @return UserProfileResponseDto 프로필 조회 내용 - 해당 사용자의 이름, 상태메시지, 팔로우 여부, 팔로우/팔로워 수, 게시글 목록
     * @throws 403 해당 페이지 접근 권한이 없기 때문에 예외 발생
     */
    @Transactional(readOnly = true)
    @Override
    public UserProfileResponseDto getProfile(Long loginUserId, Long UserId) {
        User user = userRepository.findByUserIdOrElseThrow(UserId);

        if (!loginUserId.equals(UserId)
            && followingRepository.existsByFollowingUserIdAndUserId(loginUserId, UserId)
            && !user.isPublic()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                "이 사용자는 프로필 비공개 설정 상태이며, 친구가 아닌 경우 정보 열람이 제한됩니다.");
        }

        //팔로잉, 팔로워 수 구하기
        Long followingCnt = followingRepository.countByUser_id(UserId);
        Long followerCnt = followingRepository.countByFollowingUser_id(UserId);

        if (loginUserId.equals(UserId)) {
            //게시글 가져오기 - 자기자신의 프로필이라 isn't public 한 게시글도 다 불러옴
            List<NewsFeed> posts = postRepository.getAllMyPosts(UserId);

            return new UserProfileResponseDto(
                user.getId(),
                user.getName(),
                user.getIntroduction(),
                user.isPublic(),
                followingCnt.intValue(),
                followerCnt.intValue(),
                posts
            );
        } else {
            List<NewsFeed> posts = postRepository.getVisiblePostsByUser(UserId);

            return new UserProfileResponseDto(
                user.getId(),
                user.getName(),
                user.getIntroduction(),
                user.isPublic(),
                followingCnt.intValue(),
                followerCnt.intValue(),
                followingRepository.existsByFollowingUserIdAndUserId(loginUserId, UserId),
                posts
            );
        }
    }

    public void follow(Long userId, Long followingId) {
        if (userId.equals(followingId)) {
            throw new ApiException(ErrorType.CANNOT_FOLLOW_SELF);
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(ErrorType.USER_NOT_FOUND));

        User targetUser = userRepository.findById(followingId)
                .orElseThrow(() -> new ApiException(ErrorType.USER_NOT_FOUND));

        if (followingRepository.existsByFollowingUserIdAndUserId(followingId, userId)) {
            throw new ApiException(ErrorType.ALREADY_FOLLOWING);
        }

        Following following = Following.builder()
                .followingUser(targetUser)
                .user(user)
                .build();

        followingRepository.save(following);
    }

    @Transactional
    public void unfollow(Long userId, Long followingId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(ErrorType.USER_NOT_FOUND));

        userRepository.findById(followingId)
                .orElseThrow(() -> new ApiException(ErrorType.USER_NOT_FOUND));

        if (!followingRepository.existsByFollowingUserIdAndUserId(followingId, userId)) {
            throw new ApiException(ErrorType.NOT_FOLLOWING);
        }

        Integer count = followingRepository.deleteByUserIdAndFollowingUserId(userId, followingId)
                .orElseThrow(() -> new ApiException(UNFOLLOW_FAILED));

        if (count != 1) {
            throw new ApiException(UNFOLLOW_FAILED);
        }
    }


}

