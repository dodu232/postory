package org.example.postory.domain.comment.service;

import static org.example.postory.global.error.response.ErrorType.POST_NOT_FOUND;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.example.postory.domain.comment.dto.CommentRequestDto;
import org.example.postory.domain.comment.dto.CommentResponseDto;
import org.example.postory.domain.comment.dto.CommentResponseDto.Create;
import org.example.postory.domain.comment.entity.Comment;
import org.example.postory.domain.comment.repository.CommentRepository;
import org.example.postory.domain.post.entity.Post;
import org.example.postory.domain.post.service.PostService;
import org.example.postory.domain.user.service.UserService;
import org.example.postory.global.error.ApiException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final UserService userService;
    private final PostService postService;

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
}
