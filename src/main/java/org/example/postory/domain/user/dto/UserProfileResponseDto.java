package org.example.postory.domain.user.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import org.example.postory.domain.post.dto.PostResponseDto.NewsFeed;

import java.util.List;

/**
 * 유저 프로필 반환하는 Dto
 */
@Getter
public class UserProfileResponseDto {

    //해당 사용자의 이름, 상태메시지, 팔로우 여부, 팔로우/팔로워 수, 게시글 목록
    private final Long id;

    private final String username;

    private final String introduction;

    @JsonProperty("isUserPublic")  // isUserPublic을 UserPublic으로 추론하는 에러에 대한 해결코드
    private final boolean isUserPublic;

    private final int followingCnt;

    private final int followerCnt;

    private Boolean isFollowing;

    private final int postCount;

    private final List<NewsFeed> postList;

    public UserProfileResponseDto(Long id, String username, String introduction, boolean isUserPublic,
                                  int followingCnt, int followerCnt, List<NewsFeed> postList) {
        this.id = id;
        this.username = username;
        this.introduction = introduction;
        this.isUserPublic = isUserPublic;
        this.followingCnt = followingCnt;
        this.followerCnt = followerCnt;
        this.postCount = postList.size();
        this.postList = postList;
    }

    public UserProfileResponseDto(Long id, String username, String introduction, boolean isUserPublic,
                                  int followingCnt, int followerCnt, boolean isFollowing, List<NewsFeed> postList) {
        this.id = id;
        this.username = username;
        this.introduction = introduction;
        this.isUserPublic = isUserPublic;
        this.followingCnt = followingCnt;
        this.followerCnt = followerCnt;
        this.isFollowing = isFollowing;
        this.postCount = postList.size();
        this.postList = postList;
    }
}
