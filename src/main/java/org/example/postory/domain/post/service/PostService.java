package org.example.postory.domain.post.service;

import org.example.postory.domain.post.entity.Post;

public interface PostService {

    // 게시물 id와 사용자 id로 게시물 조회
    Post getPostById(long postId, Long userId);

}
