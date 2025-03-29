package com.goorm.clonestagram.feed.dto;
import com.goorm.clonestagram.feed.domain.Feed;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
@Getter
public class FeedResponseDto {
    private final Long feedId;
    private final Long postId;
    private final Long viewerId; // 피드를 보는 사람
    private final Long authorId; // ✅ 게시글 작성자 ID 추가
    private final String username;
    private final String content;
    private final String mediaUrl;
    private final LocalDateTime createdAt;
    private final Long likeCount;

    @Builder
    public FeedResponseDto(Long feedId, Long postId, Long viewerId, Long authorId,
                           String username, String content, String mediaUrl,
                           LocalDateTime createdAt, Long likeCount) {
        this.feedId = feedId;
        this.postId = postId;
        this.viewerId = viewerId;
        this.authorId = authorId;
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
                .viewerId(feed.getUser().getId()) // 피드를 보는 유저
                .authorId(feed.getPost().getUser().getId()) // ✅ 게시글 작성자 ID
                .username(feed.getPost().getUser().getUsername())
                .content(feed.getPost().getContent())
                .mediaUrl(feed.getPost().getMediaName())
                .createdAt(feed.getCreatedAt())
                .likeCount(likeCount)
                .build();
    }
}
