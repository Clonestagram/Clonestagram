package com.goorm.clonestagram.user.service;

import com.goorm.clonestagram.file.dto.PostInfoDto;
import com.goorm.clonestagram.file.repository.PostsRepository;
import com.goorm.clonestagram.file.service.ImageService;
import com.goorm.clonestagram.follow.repository.FollowRepository;
import com.goorm.clonestagram.user.domain.User;
import com.goorm.clonestagram.file.dto.ImageUploadReqDto;
import com.goorm.clonestagram.file.dto.ImageUploadResDto;
import com.goorm.clonestagram.user.dto.UserProfileDto;
import com.goorm.clonestagram.user.dto.UserProfileUpdateDto;
import com.goorm.clonestagram.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 사용자 프로필 관련 비즈니스 로직을 처리하는 서비스 클래스
 * - 프로필 조회 및 수정 기능을 포함
 * - 프로필 수정 시 이미지 업로드 및 기타 사용자 정보 수정 처리
 */
@Service // 비즈니스 로직을 처리하는 서비스 클래스
@RequiredArgsConstructor // final로 선언된 필드의 생성자를 자동으로 생성
public class ProfileService {

    private final FollowRepository followRepository;
    private final UserRepository userRepository;  // 사용자 정보를 관리하는 리포지토리
    private final ImageService imageService;      // 이미지 업로드를 처리하는 서비스
    private final PostsRepository postsRepository;
    /*
    private final PasswordEncoder passwordEncoder;
    비밀번호는 암호화하는 과정이 로그인 및 회원가입시에 필요하므로 회원가입이나 로그인 기능 추가되면 비밀번호 조회 및 수정 추가예정
    */

    /**
     * 사용자 프로필 조회
     * @param userId 조회할 사용자의 ID
     * @return 사용자 프로필 정보
     * @throws IllegalArgumentException 사용자가 존재하지 않으면 예외 발생
     */
    public UserProfileDto getUserProfile(Long userId) {
        // 사용자 정보를 DB에서 조회, 존재하지 않으면 예외 발생

        int followerCount = followRepository.getFollowerCount(userId);
        int followingCount = followRepository.getFollowingCount(userId);


        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        List<PostInfoDto> postList = postsRepository.findAllByUserId(userId, PageRequest.of(0, 10))
                .stream()
                .map(PostInfoDto::fromEntity)
                .toList();

        // 조회된 사용자 정보를 DTO로 변환하여 반환
        return UserProfileDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .profileimg(user.getProfileimg())
                .bio(user.getBio())
                .followerCount(followerCount)
                .followingCount(followingCount)
                .posts(postList)
                .build();
    }


/**
 * 사용자 프로필 수정
 * @param userId 수정할 사용자의 ID
 * @param userProfileUpdateDto 수정할 프로필 정보
 * @return 수정된 사용자 객체
 * @throws IllegalArgumentException 사용자가 존재하지 않으면 예외 발생
 */
@Transactional // 트랜잭션을 관리하여 데이터 무결성을 보장
public User updateUserProfile(Long userId, UserProfileUpdateDto userProfileUpdateDto) {
    // 사용자 정보를 DB에서 조회, 존재하지 않으면 예외 발생
    User user = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("User not found"));

    // 사용자명 업데이트
    if (userProfileUpdateDto.getUsername() != null && !userProfileUpdateDto.getUsername().isEmpty()) {
        user.setUsername(userProfileUpdateDto.getUsername());
    }

    // 이메일 업데이트
    if (userProfileUpdateDto.getEmail() != null && !userProfileUpdateDto.getEmail().isEmpty()) {
        user.setEmail(userProfileUpdateDto.getEmail());
    }

        /*
        // 비밀번호 업데이트 (추후 기능 추가 예정)
        if (userProfileUpdateDto.getPassword() != null && !userProfileUpdateDto.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(userProfileUpdateDto.getPassword()));
        }
        */

    // 자기소개(bio) 업데이트
    if (userProfileUpdateDto.getBio() != null && !userProfileUpdateDto.getBio().isEmpty()) {
        user.setBio(userProfileUpdateDto.getBio());
    }

    // 프로필 이미지 업데이트
    if (userProfileUpdateDto.getProfileImage() != null && !userProfileUpdateDto.getProfileImage().isEmpty()) {
        // MultipartFile을 ImageUploadReqDto로 변환하여 이미지 업로드 요청
        ImageUploadReqDto imageUploadReqDto = new ImageUploadReqDto();
        imageUploadReqDto.setFile(userProfileUpdateDto.getProfileImage());  // MultipartFile을 set

        try {
            // 이미지 업로드 서비스 호출
            ImageUploadResDto imageUploadResDto = imageService.imageUpload(imageUploadReqDto, user.getId());

            // 이미지 URL 생성 (업로드된 파일 이름을 사용하여 경로 설정)
            String imageUrl = "/uploads/" + imageUploadResDto.getContent(); // 적합한 경로를 설정

            // 프로필 이미지 URL로 업데이트
            user.setProfileimg(imageUrl);
        } catch (Exception e) {
            // 예외 처리: 예외 메시지 로그 출력, 사용자에게 에러 메시지 전달 등
            e.printStackTrace();
            throw new RuntimeException("프로필 이미지 업로드 실패: " + e.getMessage());
        }
    }

    // 변경된 사용자 정보 저장 후 반환
    return userRepository.save(user);
}
}


