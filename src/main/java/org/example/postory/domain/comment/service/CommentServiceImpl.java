package org.example.postory.domain.comment.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.example.postory.domain.comment.dto.CommentResponseDto.CommentItem;

import org.example.postory.domain.comment.entity.Comment;
import org.example.postory.domain.comment.repository.CommentRepository;
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
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentrepository;
    private final PostRepository postRepository;

    @Override
    public CursorResponseDto<CommentItem> getComments(LocalDateTime cursorUpdatedAt, Long cursorId,
        Long postId, int size) {

        // 포스트의 공개 여부 확인
        Post post = postRepository.findById(postId)
            .orElseThrow(() -> new ApiException(ErrorType.POST_NOT_FOUND));
        if(!post.isPublic()) {
            throw new ApiException(ErrorType.POST_NOT_PUBLIC);
        }

        // 첫 번째 조회.
        if (cursorUpdatedAt == null || cursorId == null) {
            cursorUpdatedAt = LocalDateTime.now();
            cursorId = Long.MAX_VALUE;
        }

        // 한 번에 10개씩 가져오도록 고정.
        Pageable pageable = PageRequest.of(0, size);

        List<Comment> comments = commentrepository.getComments(cursorUpdatedAt, cursorId, postId, pageable);
        List<CommentItem> commentsDto = comments.stream().map(CommentItem::new).toList();

        // 다음 커서 정보 저장
        CursorDto nextCursor = null;
        if (!comments.isEmpty()) {
            Comment lastComment = comments.get(comments.size() - 1);
            nextCursor = new CursorDto(lastComment.getUpdatedAt(), lastComment.getId());
        }

        return CursorResponseDto.of(commentsDto, nextCursor);
    }
}
