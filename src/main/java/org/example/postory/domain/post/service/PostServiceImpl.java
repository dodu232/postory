package org.example.postory.domain.post.service;

import static org.example.postory.global.error.response.ErrorType.FORBIDDEN_POST_UPDATE;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.example.postory.domain.post.dto.PostRequestDto;
import org.example.postory.domain.post.dto.PostResponseDto;
import org.example.postory.domain.post.dto.PostResponseDto.NewsFeed;
import org.example.postory.domain.post.entity.Post;
import org.example.postory.domain.post.entity.PostLike;
import org.example.postory.domain.post.repository.PostLikeRepository;
import org.example.postory.domain.post.repository.PostRepository;
import org.example.postory.domain.user.entity.User;
import org.example.postory.domain.user.repository.UserRepository;
import org.example.postory.global.common.pagination.CursorDto;
import org.example.postory.global.common.pagination.CursorResponseDto;
import org.example.postory.global.error.ApiException;
import org.example.postory.global.error.response.ErrorType;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;


@Service
@RequiredArgsConstructor // 생성자 주입
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final PostLikeRepository postLikeRepository;
    private final RestClient.Builder builder;

    @Override
    public Post getPostById(long postId, Long userId) {
        return postRepository.findVisiblePost(postId, userId)
            .orElseThrow(() -> new ApiException(ErrorType.POST_NOT_FOUND));
        // 결과는 Optional<Post> 형식으로 반환되며, 값이 존재하면 그 값을 꺼내서 return
        // 빈 Optional이 나오면 orElseThrow로 값 던지기
    }

    @Override
    public PostResponseDto.Create createPost(PostRequestDto.Create dto, UserDetails userDetails) {
        if (userDetails == null) {  // userId가 들어있는 userDetail이 null인지 먼저 확인 (인증 실패 에러)
            throw new ApiException(ErrorType.UNAUTHORIZED_USER);
        }
        Long userId = Long.valueOf(userDetails.getUsername()); // userDetails에서 userId 추출
        User user = userRepository.findById(userId)   // userDetails에서 userId가 null인지 또 확인
            .orElseThrow(() -> new ApiException(
                ErrorType.USER_NOT_FOUND)); // db에 임의의 숫자를 입력하는 경우, 존재하지 않는 유저 에러
        Post post = Post.builder()
            .title(dto.getTitle())
            .content(dto.getContent())
            .hashtag(dto.getHashtag())
            .isPublic(dto.isPublic())
            .user(user) // DB에서 실제 user객체 조회하도록 수정
            .build();
        Post saved = postRepository.save(post);
        return PostResponseDto.Create.fromPostEntity(saved);
    }

    @Override
    @Transactional
    public void deletePost(long postId, UserDetails userDetails) {
        if (userDetails == null) {  // @AuthenticationPrincipal이 null일 때
            throw new ApiException(ErrorType.UNAUTHORIZED_USER);  // 로그인 되지 않은 사용자 접근 차단
        }
        Long userId = Long.parseLong(userDetails.getUsername());
        Post post = postRepository.findById(postId) // 삭제할 게시물이 존재하는지 db에서 조회
            .orElseThrow(() -> new ApiException(ErrorType.POST_NOT_FOUND));
        if (!post.getUser().getId().equals(userId)) { // 게시글 작성자 본인인지 확인
            throw new ApiException(ErrorType.NO_PERMISSION);
        }
        post.markAsDeleted(); // deletedAt을 현재 시각으로 기록
    }


    @Override
    public CursorResponseDto<NewsFeed> getNewsFeed(LocalDateTime cursorUpdatedAt, Long cursorId,
        int size) {

        // 첫 번째 조회.
        if (cursorUpdatedAt == null || cursorId == null) {
            cursorUpdatedAt = LocalDateTime.now();
            cursorId = Long.MAX_VALUE;
        }

        // 한 번에 10개씩 가져오도록 고정.
        Pageable pageable = PageRequest.of(0, size);

        List<Post> newsFeed = postRepository.getNewsFeed(cursorUpdatedAt, cursorId, pageable);
        List<NewsFeed> newsFeedDto = newsFeed.stream().map(NewsFeed::new).toList();

        // 다음 커서 정보 저장
        CursorDto nextCursor = null;
        if (!newsFeed.isEmpty()) {
            Post lastFeed = newsFeed.get(newsFeed.size() - 1);
            nextCursor = new CursorDto(lastFeed.getUpdatedAt(), lastFeed.getId());
        }

        return CursorResponseDto.of(newsFeedDto, nextCursor);

    }

    // 게시물 수정
    @Override
    public void updatePost(long id, PostRequestDto.Update updatePost, Long userId) {

        Post post = postRepository.findByIdOrElseThrow(id);

        // 게시물 작성자인지 확인
        if (!userId.equals(post.getId())) {
            throw new ApiException(FORBIDDEN_POST_UPDATE);
        }

        post.updatePost(updatePost);
        postRepository.save(post);
    }

    @Override
    @Transactional
    public void likePost(long postId, UserDetails userDetails) {
        long userId = Long.parseLong(userDetails.getUsername());
        Optional<PostLike> postLike = postLikeRepository.findByPostIdAndUserId(postId, userId);

        if (postLike.isPresent()) {
            postLikeRepository.delete(postLike.get());
        } else {
            User user = new User(userId);
            Post post = new Post(postId);
            postLikeRepository.save(new PostLike(user, post));
        }
    }


    // 삭제되지 않은 게시글 + 수정일 기준 최신 정렬 ( 함수이름 가독성이 좋지않아서 따로 함더감쌌음)
    public List<NewsFeed> getAllMyPosts(Long userId) {
        return postRepository.getAllByUser_IdAndDeletedAtIsNullOrderByUpdatedAt(userId)
            .stream().map(PostResponseDto.NewsFeed::new).collect(Collectors.toList());
    }

    //공개 게시글 + 삭제되지 않은 게시글 + 수정일 기준 최신순 정렬
    public List<NewsFeed> getVisiblePostsByUser(Long userId) {
        return postRepository.getAllByUser_IdAndDeletedAtIsNullAndIsPublicIsTrueOrderByUpdatedAt(
                userId)
            .stream().map(PostResponseDto.NewsFeed::new).collect(Collectors.toList());
    }
}
