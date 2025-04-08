package org.example.postory.domain.post.repository;

import java.util.List;
import java.util.stream.Collectors;
import org.antlr.v4.runtime.atn.SemanticContext.AND;
import org.example.postory.domain.post.dto.PostResponseDto;
import org.example.postory.domain.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostRepository extends JpaRepository<Post, Long> {

    //공개 게시글 + 삭제되지 않은 게시글 + 수정일 기준 최신순 정렬
    List<Post> getAllByUser_IdAndDeletedAtIsNullAndIsPublicIsTrueOrderByUpdatedAt(Long userId);

    default List<PostResponseDto> getVisiblePostsByUser(Long userId) {
        return getAllByUser_IdAndDeletedAtIsNullAndIsPublicIsTrueOrderByUpdatedAt(userId)
            .stream().map(PostResponseDto::new).collect(Collectors.toList());
    }


    List<Post> getAllByUser_IdAndDeletedAtIsNullOrderByUpdatedAt(Long userId);

    // 삭제되지 않은 게시글 + 수정일 기준 최신 정렬 ( 함수이름 가독성이 좋지않아서 따로 함더감쌌음)
    default List<PostResponseDto> getAllMyPosts(Long userId){
        return getAllByUser_IdAndDeletedAtIsNullOrderByUpdatedAt(userId)
            .stream().map(PostResponseDto::new).collect(Collectors.toList());
    }
}