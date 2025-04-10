package org.example.postory.domain.comment.service;

import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.postory.domain.comment.dto.CommentRequestDto;
import org.example.postory.domain.comment.dto.CommentResponseDto;
import org.example.postory.domain.comment.dto.CommentResponseDto.CommentItem;
import org.example.postory.domain.comment.dto.CommentResponseDto.Create;
import org.example.postory.domain.comment.entity.Comment;
import org.example.postory.domain.comment.repository.CommentRepository;
import org.example.postory.domain.post.entity.Post;
import org.example.postory.domain.post.repository.PostRepository;
import org.example.postory.domain.post.service.PostService;
import org.example.postory.domain.user.service.UserService;
import org.example.postory.global.common.pagination.CursorDto;
import org.example.postory.global.common.pagination.CursorResponseDto;
import org.example.postory.global.error.ApiException;
import org.example.postory.global.error.response.ErrorType;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final UserService userService;
    private final PostService postService;
    private final PostRepository postRepository;
  
    /**
     * [service] 댓글 생성 메서드. 해당 게시글이 사용자에게 보이는 게시글인지 검증한 후, 댓글 내용을 기반으로 Comment 엔티티를 생성하고 저장. 저장된 댓글
     * 정보를 기반으로 댓글 응답 DTO를 반환/
     *
     * @param authUserId 댓글을 작성하려는 인증된 사용자 ID
     * @param requestDto 댓글 내용을 담은 요청 DTO
     * @param postId     댓글이 작성될 게시글의 ID
     * @return 생성된 댓글 정보를 담은 CommentResponseDto.Create 객체
     * @throws ApiException 게시글이 존재하지 않거나 접근 권한이 없는 경우 예외 발생
     */
    @Override
    public CommentResponseDto.Create createComment(long authUserId,
        CommentRequestDto.Create requestDto, Long postId) {
        Post findPost = postService.getPostById(postId, authUserId);

        Comment comment = Comment.builder().content(requestDto.getContents())
            .user(userService.getById(authUserId)).post(findPost).build();
        Comment savedComment = commentRepository.save(comment);
        return new Create(savedComment);
    }
      
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

        List<Comment> comments = commentRepository.getComments(cursorUpdatedAt, cursorId, postId,
            pageable);
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
