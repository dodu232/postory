package org.example.postory.domain.post.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SearchType {
    NORMAL("normal"),
    MENTION("mention"),
    HASHTAG("hashtag");

    private final String type;

}
