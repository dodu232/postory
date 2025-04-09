package org.example.postory.domain.post.service;

import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.postory.domain.post.dto.PostResponseDto.NewsFeed;
import org.example.postory.domain.post.entity.Post;
import org.example.postory.domain.post.repository.PostRepository;
import org.example.postory.global.common.pagination.CursorDto;
import org.example.postory.global.common.pagination.CursorResponseDto;
import org.example.postory.global.error.ApiException;
import org.example.postory.global.error.response.ErrorType;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor // 생성자 주입
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;

    @Override
    public Post getPostById(long postId, Long userId) {
        return postRepository.findVisiblePost(postId, userId)
                .orElseThrow(() -> new ApiException(ErrorType.POST_NOT_FOUND));
        // 결과는 Optional<Post> 형식으로 반환되며, 값이 존재하면 그 값을 꺼내서 return
        // 빈 Optional이 나오면 orElseThrow로 값 던지기
    }

    @Override
    public CursorResponseDto<NewsFeed> getNewsFeed(LocalDateTime cursorUpdatedAt, Long cursorId, int size) {

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


}
