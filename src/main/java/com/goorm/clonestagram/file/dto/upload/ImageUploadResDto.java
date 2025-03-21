package com.goorm.clonestagram.file.dto.upload;

import com.goorm.clonestagram.file.ContentType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * 이미지 업로드 응답을 위한 DTO
 * - content, type, createdAt을 반환
 */
@Getter
@Builder
public class ImageUploadResDto {
    private String content;
    private ContentType type;
    private LocalDateTime createdAt;
}
