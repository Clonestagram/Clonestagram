package com.goorm.clonestagram.user.dto;

import com.goorm.clonestagram.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * 프로필 조회를 위한 DTO
 * 프로필에 필요한 개인정보를 모두 반환
 */
@Getter
@Builder
@AllArgsConstructor
public class UserProfileDto {
    private Long id;
    private String username;
    private String email;
    private String bio;
    private String profileimg;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // ✅ User 엔티티를 UserProfileDto로 변환하는 메서드
    public static UserProfileDto fromEntity(User user) {
        return UserProfileDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .bio(user.getBio())
                .profileimg(user.getProfileimg())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
}
