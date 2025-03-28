package com.goorm.clonestagram.post.dto.upload;

import com.goorm.clonestagram.post.ContentType;
import com.goorm.clonestagram.post.domain.Posts;
import com.goorm.clonestagram.user.domain.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 이미지 업로드 요청을 위한 DTO
 * - file, content, type을 클라이언트에게 받음
 * - createdAt을 생성
 */
@Getter
@Setter
@NoArgsConstructor
public class VideoUploadReqDto {

    private String file;
    private String content;
    private List<String> hashTagList;


    public Posts toEntity(String imageName , User user) {
        return Posts.builder()
                .user(user)
                .content(content)
                .mediaName(imageName)
                .contentType(ContentType.VIDEO)
                .build();
    }
}
