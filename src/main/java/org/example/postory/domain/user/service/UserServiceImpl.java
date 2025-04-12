package org.example.postory.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.example.postory.domain.comment.entity.Comment;
import org.example.postory.domain.comment.repository.CommentLikeRepository;
import org.example.postory.domain.comment.repository.CommentRepository;
import org.example.postory.domain.post.dto.PostResponseDto;
import org.example.postory.domain.post.dto.PostResponseDto.NewsFeed;
import org.example.postory.domain.post.entity.Post;
import org.example.postory.domain.post.repository.PostLikeRepository;
import org.example.postory.domain.post.repository.PostRepository;
import org.example.postory.domain.user.dto.*;
import org.example.postory.domain.user.dto.UserRequestDto.UpdateProfile;
import org.example.postory.domain.user.entity.Following;
import org.example.postory.domain.user.entity.User;
import org.example.postory.domain.user.repository.FollowingRepository;
import org.example.postory.domain.user.repository.UserRepository;
import org.example.postory.global.common.pagination.CursorDto;
import org.example.postory.global.common.pagination.CursorResponseDto;
import org.example.postory.global.error.ApiException;
import org.example.postory.global.error.response.ErrorType;
import org.example.postory.global.util.PasswordEncoder;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static org.example.postory.global.error.response.ErrorType.*;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final FollowingRepository followingRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    private final PostLikeRepository postLikeRepository;
    private final CommentLikeRepository commentLikeRepository;

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

        User user = User.builder()
                .email(requestDto.getEmail())
                .password(PasswordEncoder.encode(requestDto.getPassword()))
                .name(requestDto.getName())
                .phone(requestDto.getPhone())
                .build();

        User savedUser = userRepository.save(user);
        return new SignupResponseDto(savedUser.getId());
    }

    /**
     * [Service] 프로필 조회 함수 1. controller에서 받아온 유저값 검증 2. 다른사람의 프로필 + 팔로우 안했음 + 상대방이 프로필 비공개 상태 ->
     * 403 3. 자신의 프로필 조회일 경우 - 비공개 개시글 표시o, 팔로잉 여부 표시x 4. 타인의 프로필 조회일 경우 - 비공개 게시글 표시x, 팔로잉 여부 표시o
     *
     * @param userDetails 현재 로그인 중인 유저 정보
     * @param UserId      프로필 조회할 유저 아이디
     * @return UserProfileResponseDto 프로필 조회 내용 - 해당 사용자의 이름, 상태메시지, 팔로우 여부, 팔로우/팔로워 수, 게시글 목록
     * @throws 403 해당 페이지 접근 권한이 없기 때문에 예외 발생
     */
    @Transactional(readOnly = true)
    @Override
    public UserProfileResponseDto getProfile(UserDetails userDetails, Long UserId) {
        if (userDetails != null) {
            Long loginUserId = Long.parseLong(userDetails.getUsername());
            User user = userRepository.findByUserIdOrElseThrow(UserId);

            if (user.getDeletedAt() != null) {
                throw new ApiException(DISABLE_USER);
            }

            if (!loginUserId.equals(UserId) && !followingRepository.existsByUserIdAndFollowingUserId(
                    loginUserId, UserId) && !user.isUserPublic()) {
                throw new ApiException(FORBIDDEN_PROFILE);
            }

            //팔로잉, 팔로워 수 구하기
            Long followingCnt = followingRepository.countByUser_id(UserId);
            Long followerCnt = followingRepository.countByFollowingUser_id(UserId);

            if (loginUserId.equals(UserId)) {
                //게시글 가져오기 - 자기자신의 프로필이라 isn't public 한 게시글도 다 불러옴
                List<NewsFeed> posts = postRepository.getAllMyPosts(loginUserId);

                return new UserProfileResponseDto(user.getId(), user.getName(), user.getIntroduction(),
                        user.isUserPublic(), followingCnt.intValue(), followerCnt.intValue(), posts);
            } else {
                List<NewsFeed> posts = postRepository.getAllByUser_IdAndDeletedAtIsNullAndIsPostPublicIsTrueOrderByUpdatedAt(
                                UserId)
                        .stream().map(PostResponseDto.NewsFeed::new).collect(Collectors.toList());

                return new UserProfileResponseDto(user.getId(), user.getName(), user.getIntroduction(),
                        user.isUserPublic(), followingCnt.intValue(), followerCnt.intValue(),
                        followingRepository.existsByUserIdAndFollowingUserId(loginUserId, UserId), posts);
            }
        } else {
            return getProfileByNonLoginUser(UserId);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public UserProfileResponseDto getProfileByNonLoginUser(Long UserId) {
        User user = userRepository.findByUserIdOrElseThrow(UserId);

        if (user.getDeletedAt() != null) {
            throw new ApiException(DISABLE_USER);
        }

        if (!user.isUserPublic()) {
            throw new ApiException(FORBIDDEN_PROFILE);
        }

        //팔로잉, 팔로워 수 구하기
        Long followingCnt = followingRepository.countByUser_id(UserId);
        Long followerCnt = followingRepository.countByFollowingUser_id(UserId);

        List<NewsFeed> posts = postRepository.getVisiblePostsByUser(UserId);

        return new UserProfileResponseDto(user.getId(), user.getName(), user.getIntroduction(),
                user.isUserPublic(), followingCnt.intValue(), followerCnt.intValue(), false, posts);
    }

    /**
     * [Service] 프로필 정보 업데이트 함수 업데이트된 데이터는 userId로 기존 정보를 가져와 필요한 값만 변경 후 저장됩니다. 비밀번호는 기존 비밀번호와 다를
     * 경우에만 변경됩니다.
     *
     * @param userId  업데이트 대상 사용자 ID
     * @param profile 업데이트할 프로필 정보 (name, introduction, gender, password, isPostPublic)
     * @return 업데이트된 사용자 프로필 정보
     */
    @Transactional
    @Override
    public UserResponseDto.UpdateProfile updateProfile(Long userId, UpdateProfile profile) {
        User user = userRepository.findByUserIdOrElseThrow(userId);

        if (user.getDeletedAt() != null) {
            throw new ApiException(DISABLE_USER);
        }

        user.updateProfile(profile);
        return new UserResponseDto.UpdateProfile(user);
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

        Integer count = followingRepository.deleteByUserIdAndFollowingUserId(loginUserId,
                        followingId)
                .orElseThrow(() -> new ApiException(UNFOLLOW_FAILED));

        if (count != 1) {
            throw new ApiException(UNFOLLOW_FAILED);
        }
    }


    public CursorResponseDto<FollowingResponseDto> getFollowing(UserDetails userDetails, Long userId, Long cursorId, int size) {
        authenticationValidate(userDetails, userId);

        // 로그인 - 나 자신이면 조회 가능, 나 자신이 아니고 공개면 조회 가능, 나 자신이 아니고 비공개인데 친구이면 조회 가능
        // 비로그인 - 공개면 조회 가능
        if (cursorId == null) {
            cursorId = Long.MAX_VALUE;
        }

        Pageable pageable = PageRequest.of(0, size);

        // 커서 기반으로 팔로잉 유저 목록 조회 (최근에 팔로우한 순)
        List<Following> followings = followingRepository.findFollowingsByCursor(userId, cursorId,
                pageable);

        List<FollowingResponseDto> followingResponseDtos = followings.stream()
                .map(f -> new FollowingResponseDto(f.getFollowingUser().getId(),
                        f.getFollowingUser().getName()))
                .collect(Collectors.toList());

        // 다음 커서 설정
        CursorDto nextCursor = getCursorDto(followings);

        return CursorResponseDto.of(followingResponseDtos, nextCursor);
    }


    public CursorResponseDto<FollowingResponseDto> getFollowers(UserDetails userDetails, Long userId, Long cursorId, int size) {
        authenticationValidate(userDetails, userId);

        // 로그인 - 나 자신이면 조회 가능, 나 자신이 아니고 공개면 조회 가능, 나 자신이 아니고 비공개인데 친구이면 조회 가능
        // 비로그인 - 공개면 조회 가능
        if (cursorId == null) {
            cursorId = Long.MAX_VALUE;
        }

        Pageable pageable = PageRequest.of(0, size);

        // 커서 기반으로 팔로잉 유저 목록 조회 (최근에 팔로우한 순)
        List<Following> followers = followingRepository.findFollowersByCursor(userId, cursorId, pageable);

        List<FollowingResponseDto> followingResponseDtos = followers.stream()
                .map(f -> new FollowingResponseDto(f.getUser().getId(), f.getUser().getName()))
                .collect(Collectors.toList());

        CursorDto nextCursor = getCursorDto(followers);

        return CursorResponseDto.of(followingResponseDtos, nextCursor);
    }

    private static CursorDto getCursorDto(List<Following> followers) {
        // 다음 커서 설정
        CursorDto nextCursor = null;
        if (!followers.isEmpty()) {
            Long lastId = followers.get(followers.size() - 1).getId();
            nextCursor = new CursorDto(lastId); // 팔로잉 목록은 정렬 기준이 최근 업데이트된 순이 아니라 팔로잉 순서이기 때문에 id만 사용
        }
        return nextCursor;
    }

    private void authenticationValidate(UserDetails userDetails, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(USER_NOT_FOUND));

        if (userDetails != null) {  // 로그인
            Long loginUserId = Long.parseLong(userDetails.getUsername());

            userRepository.findById(loginUserId)
                    .orElseThrow(() -> new ApiException(ErrorType.USER_NOT_FOUND));

            // 나 자신이 아니고 비공개이고 친구가 아니면 조회 불가
            if (!loginUserId.equals(userId)
                    && !user.isUserPublic()
                    && !followingRepository.existsByUserIdAndFollowingUserId(loginUserId, userId)) {
                throw new ApiException(FORBIDDEN_PROFILE);
            }
        } else {  // 비로그인
            if (!user.isUserPublic()) {
                throw new ApiException(FORBIDDEN_PROFILE);
            }
        }
    }

    @Override
    public User getById(Long authUserId) {
        return userRepository.findByUserIdOrElseThrow(authUserId);
    }

    /**
     * [Service] 계정비활성화하기
     *
     * @param authUserId 로그인 한 사용자
     */
    @Transactional
    @Override
    public void deactivateUser(Long authUserId) {
        //사용자 비활성화
        User user = getById(authUserId);

        if (user.getDeletedAt() != null) {
            throw new ApiException(ALREADY_DEACTIVATED_ACCOUNT);
        }
        user.markAsDeleted();

        //해당 유저가 표시한 게시글 좋아요 삭제
        //postLikeRepository.deleteAllByUser_Id(authUserId);
        //게시글 모두 비활성화
        List<Post> myAllPosts = postRepository.getAllByUser_IdAndDeletedAtIsNullOrderByUpdatedAt(
                authUserId);
        for (Post post : myAllPosts) {
            post.markAsDeleted();
            //해당 게시글이나 유저와 관련된 게시글 좋아요 삭제
            //postLikeRepository.deleteAllByPost_Id(post.getId());
            commentRepository.findAllByPost_Id(post.getId()).forEach(Comment::markAsDeleted);
        }

        //해당 유저가 표시한 댓글 좋아요 삭제
        //commentLikeRepository.deleteAllByUser_Id(authUserId);
        //덧글 모두 비활성화
        List<Comment> myAllComments = commentRepository.getAllByUser_IdAndDeletedAtIsNull(
                authUserId);
        myAllComments.forEach(Comment::markAsDeleted);
        for (Comment comment : myAllComments) {
            comment.markAsDeleted();
            //해당 게시글이나 유저와 관련된 게시글 좋아요 삭제
            //commentLikeRepository.deleteAllByComment_Id(comment.getId());
        }

        //유저의 팔로우관계 모두 종료
        followingRepository.deleteAllByUser_Id(authUserId); //팔로잉
        followingRepository.deleteAllByFollowingUser_Id(authUserId); // 팔로워
    }
}

