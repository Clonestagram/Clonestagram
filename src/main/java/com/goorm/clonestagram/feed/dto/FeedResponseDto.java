package com.goorm.clonestagram.feed.dto;
import com.goorm.clonestagram.feed.domain.Feed;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class FeedResponseDto {
    private final Long feedId;
    private final Long postId;
    private final Long userId;
    private final String username;
    private final String content;
    private final String mediaUrl;
    private final LocalDateTime createdAt;
    private final Long likeCount; // ✅ 추가

    @Builder
    public FeedResponseDto(Long feedId, Long postId, Long userId, String username,
                           String content, String mediaUrl, LocalDateTime createdAt,
                           Long likeCount) {
        this.feedId = feedId;
        this.postId = postId;
        this.userId = userId;
        this.username = username;
        this.content = content;
        this.mediaUrl = mediaUrl;
        this.createdAt = createdAt;
        this.likeCount = likeCount;
    }

    public static FeedResponseDto from(Feed feed, Long likeCount) {
        return FeedResponseDto.builder()
                .feedId(feed.getId())
                .postId(feed.getPost().getId())
                .userId(feed.getUser().getId())
                .username(feed.getPost().getUser().getUsername())
                .content(feed.getPost().getContent())
                .mediaUrl(feed.getPost().getMediaName())
                .createdAt(feed.getCreatedAt())
                .likeCount(likeCount)
                .build();
    }
}
