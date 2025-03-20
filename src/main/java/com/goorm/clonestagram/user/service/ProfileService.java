package com.goorm.clonestagram.user.service;

import com.goorm.clonestagram.file.service.ImageService;
import com.goorm.clonestagram.user.domain.User;
import com.goorm.clonestagram.file.dto.ImageUploadReqDto;
import com.goorm.clonestagram.file.dto.ImageUploadResDto;
import com.goorm.clonestagram.user.dto.UserProfileDto;
import com.goorm.clonestagram.user.dto.UserProfileUpdateDto;
import com.goorm.clonestagram.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 사용자 프로필 관련 비즈니스 로직을 처리하는 서비스 클래스
 * - 프로필 조회 및 수정 기능을 포함
 * - 프로필 수정 시 이미지 업로드 및 기타 사용자 정보 수정 처리
 */
@Service // 비즈니스 로직을 처리하는 서비스 클래스
@RequiredArgsConstructor // final로 선언된 필드의 생성자를 자동으로 생성
public class ProfileService {

    private final UserRepository userRepository;  // 사용자 정보를 관리하는 리포지토리
    private final ImageService imageService;      // 이미지 업로드를 처리하는 서비스
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
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // 조회된 사용자 정보를 DTO로 변환하여 반환
        return new UserProfileDto(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getBio(),
                user.getProfileimg(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }

    /**
     * 사용자 프로필 수정
     * @param userId 수정할 사용자의 ID
     * @param userProfileUpdateDto 수정할 프로필 정보
     * @return 수정된 사용자 객체
     * @throws IllegalArgumentException 사용자가 존재하지 않으면 예외 발생
     */
    @Transactional
    public UserProfileDto updateUserProfile(Long userId, UserProfileUpdateDto userProfileUpdateDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (userProfileUpdateDto.getUsername() != null) {
            user.setUsername(userProfileUpdateDto.getUsername());
        }
        if (userProfileUpdateDto.getEmail() != null) {
            user.setEmail(userProfileUpdateDto.getEmail());
        }
        if (userProfileUpdateDto.getBio() != null) {
            user.setBio(userProfileUpdateDto.getBio());
        }

        User updatedUser = userRepository.save(user);

        // ✅ DTO로 변환하여 반환
        return UserProfileDto.fromEntity(updatedUser);
    }




}
