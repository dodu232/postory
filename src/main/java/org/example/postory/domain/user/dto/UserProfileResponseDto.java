package org.example.postory.domain.user.dto;


import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.example.postory.domain.post.dto.PostResponseDto;
import org.example.postory.domain.post.entity.Post;

/**
 * 유저 프로필 반환하는 Dto
 */
@Getter
public class UserProfileResponseDto {
    //해당 사용자의 이름, 상태메시지, 팔로우 여부, 팔로우/팔로워 수, 게시글 목록
    private final Long id;

    private final String username;

    private final String introduction;

    private final boolean isPublic;

    private final int followingCnt;

    private final int followerCnt;

    private boolean isFollowing;

    private final int postCount;

    private final List<PostResponseDto> postList;

    public UserProfileResponseDto(Long id, String username, String introduction, boolean isPublic,
        int followingCnt, int followerCnt, List<PostResponseDto> postList) {
        this.id = id;
        this.username = username;
        this.introduction = introduction;
        this.isPublic = isPublic;
        this.followingCnt = followingCnt;
        this.followerCnt = followerCnt;
        this.postCount = postList.size();
        this.postList = postList;
    }

    public UserProfileResponseDto(Long id, String username, String introduction, boolean isPublic,
        int followingCnt, int followerCnt, boolean isFollowing, List<PostResponseDto> postList) {
        this.id = id;
        this.username = username;
        this.introduction = introduction;
        this.isPublic = isPublic;
        this.followingCnt = followingCnt;
        this.followerCnt = followerCnt;
        this.isFollowing = isFollowing;
        this.postCount = postList.size();
        this.postList = postList;
    }
}
