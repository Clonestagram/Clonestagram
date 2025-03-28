package com.goorm.clonestagram.feed.controller;

import com.goorm.clonestagram.feed.dto.FeedResponseDto;
import com.goorm.clonestagram.feed.service.FeedService;
import com.goorm.clonestagram.util.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/feed")
public class FeedController {

    private final FeedService feedService;

    /**
     * ✅ 로그인한 사용자의 피드 조회 (페이징)
     * 예: GET /api/feed?page=0&size=20
     */
    @GetMapping
    public ResponseEntity<Page<FeedResponseDto>> getMyFeed(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Page<FeedResponseDto> feedPage = feedService.getUserFeed(userDetails.getId(), page, size);
        return ResponseEntity.ok(feedPage);
    }

    /**
     * ✅ 사용자가 본 게시물 삭제
     * 예: DELETE /api/feed/seen
     * body: { "postIds": [1, 2, 3] }
     */
    @DeleteMapping("/seen")
    public ResponseEntity<Void> removeSeenFeeds(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody @Valid SeenRequest request
    ) {
        feedService.removeSeenFeeds(userDetails.getId(), request.getPostIds());
        return ResponseEntity.noContent().build();
    }

    // ✅ Request DTO
    public static class SeenRequest {
        private List<Long> postIds;

        public List<Long> getPostIds() {
            return postIds;
        }

        public void setPostIds(List<Long> postIds) {
            this.postIds = postIds;
        }
    }
}

