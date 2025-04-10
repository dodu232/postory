package org.example.postory.domain.user.service;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.example.postory.domain.post.dto.PostResponseDto.NewsFeed;
import org.example.postory.domain.post.repository.PostRepository;
import org.example.postory.domain.user.dto.*;
import org.example.postory.domain.post.service.PostService;
import org.example.postory.domain.post.service.PostService;
import org.example.postory.domain.user.dto.SignupRequestDto;
import org.example.postory.domain.user.dto.SignupResponseDto;
import org.example.postory.domain.user.dto.UserRequestDto.UpdateProfile;
import org.example.postory.global.common.pagination.CursorDto;
import org.example.postory.global.common.pagination.CursorResponseDto;
import org.example.postory.global.util.PasswordEncoder;

import static org.example.postory.global.error.response.ErrorType.*;

import org.example.postory.domain.user.dto.UserProfileResponseDto;
import org.example.postory.domain.user.entity.Following;
import org.example.postory.domain.user.entity.User;
import org.example.postory.domain.user.repository.FollowingRepository;
import org.example.postory.domain.user.repository.UserRepository;
import org.example.postory.global.error.ApiException;
import org.example.postory.global.error.response.ErrorType;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static org.example.postory.global.error.response.ErrorType.*;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final FollowingRepository followingRepository;
    private final PostService postService;

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
            .orElseThrow(() -> new ApiException(USER_NOT_FOUND));
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

        User user = User.builder().email(requestDto.getEmail())
            .password(PasswordEncoder.encode(requestDto.getPassword())).phone(requestDto.getPhone())
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

        if (!loginUserId.equals(UserId) && !followingRepository.existsByUserIdAndFollowingUserId(
            loginUserId, UserId) && !user.isPublic()) {
            throw new ApiException(FORBIDDEN_PROFILE);
        }

        //팔로잉, 팔로워 수 구하기
        Long followingCnt = followingRepository.countByUser_id(UserId);
        Long followerCnt = followingRepository.countByFollowingUser_id(UserId);

        if (loginUserId.equals(UserId)) {
            //게시글 가져오기 - 자기자신의 프로필이라 isn't public 한 게시글도 다 불러옴
            List<NewsFeed> posts = postService.getAllMyPosts(UserId);

            return new UserProfileResponseDto(user.getId(), user.getName(), user.getIntroduction(),
                user.isPublic(), followingCnt.intValue(), followerCnt.intValue(), posts);
        } else {
            List<NewsFeed> posts = postService.getVisiblePostsByUser(UserId);

            return new UserProfileResponseDto(user.getId(), user.getName(), user.getIntroduction(),
                user.isPublic(), followingCnt.intValue(), followerCnt.intValue(),
                followingRepository.existsByUserIdAndFollowingUserId(loginUserId, UserId), posts);
        }
    }

    /**
     * [Service] 프로필 정보 업데이트 함수 업데이트된 데이터는 userId로 기존 정보를 가져와 필요한 값만 변경 후 저장됩니다. 비밀번호는 기존 비밀번호와 다를
     * 경우에만 변경됩니다.
     *
     * @param userId  업데이트 대상 사용자 ID
     * @param profile 업데이트할 프로필 정보 (name, introduction, gender, password, isPublic)
     * @return 업데이트된 사용자 프로필 정보
     */
    @Transactional
    @Override
    public UserResponseDto.UpdateProfile updateProfile(Long userId, UpdateProfile profile) {
        User user = userRepository.findByUserIdOrElseThrow(userId);

        if (profile.getName() != null) {
            user.setName(profile.getName());
        }
        if (profile.getIntroduction() != null) {
            user.setIntroduction(profile.getIntroduction());
        }
        if (profile.getGender() != null) {
            user.setGender(profile.getGender());
        }

        if (profile.getPassword() != null && !PasswordEncoder.matches(profile.getPassword(),
            user.getPassword())) {
            user.setPassword(PasswordEncoder.encode(profile.getPassword()));
        }

        if (profile.getIsPublic() != null) {
            user.setPublic(profile.getIsPublic());
        }

        User savedUser = userRepository.save(user);
        return new UserResponseDto.UpdateProfile(savedUser);
    }

    public void follow(Long loginUserId, Long followingId) {
        if (loginUserId.equals(followingId)) {

            throw new ApiException(ErrorType.CANNOT_FOLLOW_SELF);
        }

        User user = userRepository.findById(loginUserId)
                .orElseThrow(() -> new ApiException(ErrorType.USER_NOT_FOUND));

        User targetUser = userRepository.findById(followingId)
                .orElseThrow(() -> new ApiException(ErrorType.USER_NOT_FOUND));

        if (followingRepository.existsByUserIdAndFollowingUserId(loginUserId, followingId)) {
            throw new ApiException(ErrorType.ALREADY_FOLLOWING);
        }

        Following following = Following.builder()
                .followingUser(targetUser)
                .user(user)
                .build();

        followingRepository.save(following);
    }

    @Transactional
    public void unfollow(Long loginUserId, Long followingId) {
        userRepository.findById(loginUserId)
                .orElseThrow(() -> new ApiException(ErrorType.USER_NOT_FOUND));

        userRepository.findById(followingId)
                .orElseThrow(() -> new ApiException(ErrorType.USER_NOT_FOUND));

        if (!followingRepository.existsByUserIdAndFollowingUserId(loginUserId, followingId)) {
            throw new ApiException(ErrorType.NOT_FOLLOWING);
        }

        Integer count = followingRepository.deleteByUserIdAndFollowingUserId(loginUserId, followingId)
                .orElseThrow(() -> new ApiException(UNFOLLOW_FAILED));

        if (count != 1) {
            throw new ApiException(UNFOLLOW_FAILED);
        }
    }

    public CursorResponseDto<FollowingResponseDto> getFollowing(Long loginUserId, Long userId, Long cursorId, int size) {
        userRepository.findById(loginUserId)
                .orElseThrow(() -> new ApiException(ErrorType.USER_NOT_FOUND));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(USER_NOT_FOUND));

        // 나 자신이 아니고 비공개이고 친구가 아니면 조회 불가
        if (!loginUserId.equals(userId)
                && !user.isPublic()
                && !followingRepository.existsByUserIdAndFollowingUserId(loginUserId, userId)) {
            throw new ApiException(FORBIDDEN_PROFILE);
        }

        // 나 자신이면 조회 가능, 나 자신이 아니고 비공개가 아니면 조회 가능, 나 자신이 아니고 비공개인데 친구이면 조회 가능
        if (cursorId == null) {
            cursorId = Long.MAX_VALUE;
        }

        Pageable pageable = PageRequest.of(0, size);

        // 커서 기반으로 팔로잉 유저 목록 조회 (최근에 팔로우한 순)
        List<Following> followings = followingRepository.findFollowingsByCursor(userId, cursorId, pageable);

        List<FollowingResponseDto> followingResponseDtos = followings.stream()
                .map(f -> new FollowingResponseDto(f.getFollowingUser().getId(), f.getFollowingUser().getName()))
                .collect(Collectors.toList());

        // 다음 커서 설정
        CursorDto nextCursor = null;
        if (!followings.isEmpty()) {
            Long lastId = followings.get(followings.size() - 1).getId();
            nextCursor = new CursorDto(lastId); // 팔로잉 목록은 정렬 기준이 최근 업데이트된 순이 아니라 팔로잉 순서이기 때문에 id만 사용
        }

        return CursorResponseDto.of(followingResponseDtos, nextCursor);
    }

    public CursorResponseDto<FollowingResponseDto> getFollowers(Long loginUserId, Long userId, Long cursorId, int size) {
        userRepository.findById(loginUserId)
                .orElseThrow(() -> new ApiException(ErrorType.USER_NOT_FOUND));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(ErrorType.USER_NOT_FOUND));

        // 나 자신이 아니고 비공개이고 친구가 아니면 조회 불가
        if (!loginUserId.equals(userId)
                && !user.isPublic()
                && !followingRepository.existsByUserIdAndFollowingUserId(loginUserId, userId)) {
            throw new ApiException(FORBIDDEN_PROFILE);
        }

        // 나 자신이면 조회 가능, 나 자신이 아니고 비공개가 아니면 조회 가능, 나 자신이 아니고 비공개인데 친구이면 조회 가능
        if (cursorId == null) {
            cursorId = Long.MAX_VALUE;
        }

        Pageable pageable = PageRequest.of(0, size);

        // 커서 기반으로 팔로잉 유저 목록 조회 (최근에 팔로우한 순)
        List<Following> followings = followingRepository.findFollowersByCursor(userId, cursorId, pageable);

        List<FollowingResponseDto> followingResponseDtos = followings.stream()
                .map(f -> new FollowingResponseDto(f.getUser().getId(), f.getUser().getName()))
                .collect(Collectors.toList());

        // 다음 커서 설정
        CursorDto nextCursor = null;
        if (!followings.isEmpty()) {
            Long lastId = followings.get(followings.size() - 1).getId();
            nextCursor = new CursorDto(lastId); // 팔로잉 목록은 정렬 기준이 최근 업데이트된 순이 아니라 팔로잉 순서이기 때문에 id만 사용
        }

        return CursorResponseDto.of(followingResponseDtos, nextCursor);
    }

}

