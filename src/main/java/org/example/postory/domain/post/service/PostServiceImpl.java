package org.example.postory.domain.post.service;

import lombok.RequiredArgsConstructor;
import org.example.postory.domain.post.entity.Post;
import org.example.postory.domain.post.repository.PostRepository;
import org.example.postory.global.error.ApiException;
import org.example.postory.global.error.response.ErrorType;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

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

}
