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
import org.example.postory.domain.post.repository.PostRepository;
import org.example.postory.domain.user.repository.UserRepository;
import org.example.postory.global.error.ApiException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    @Override
    public CommentResponseDto.Create createComment(long authUserId, CommentRequestDto.Create requestDto,
        Long postId) {
        //코멘트 생성하려는 포스트가 접근가능한지 확인
        Optional<Post> findPost = postRepository.findVisiblePost(postId,
            authUserId);

        if (findPost.isEmpty()) {
            throw new ApiException(POST_NOT_FOUND);
        }

        // requestDto로 코멘트 객체 생성
        Comment comment = Comment.builder().content(requestDto.getContents())
            .user(userRepository.findByUserIdOrElseThrow(authUserId))
            .post(findPost.get())
            .build();
        //저장
        Comment savedComment = commentRepository.save(comment);
        //반환형대로 만들어줌
        return new Create(savedComment);
    }
}
