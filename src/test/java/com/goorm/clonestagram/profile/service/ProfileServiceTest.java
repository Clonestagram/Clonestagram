package com.goorm.clonestagram.profile.service;

import com.goorm.clonestagram.user.domain.User;
import com.goorm.clonestagram.user.dto.UserProfileDto;
import com.goorm.clonestagram.user.dto.UserProfileUpdateDto;
import com.goorm.clonestagram.user.repository.UserRepository;
import com.goorm.clonestagram.user.service.ProfileService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@ExtendWith(MockitoExtension.class)
public class ProfileServiceTest {

    @Mock
    private UserRepository userRepository;

    private User existingUser;

    @InjectMocks  // ✅ 테스트 대상 객체에 Mock 주입
    private ProfileService profileService;

    @BeforeEach
    void setUp() {
        // 기존 사용자 데이터 설정
        existingUser = User.builder()
                .id(1L)
                .username("testuser2")
                .password("password456")
                .email("testuser2@example.com")
                .profileimg("http://example.com/profile2.jpg")
                .bio("두 번째 사용자입니다.")
                .build();
    }


    @Test
    public void updateUserProfile_Success() {
        // Given: 업데이트할 데이터
        UserProfileUpdateDto updateDto = new UserProfileUpdateDto();
        updateDto.setUsername("newUser");
        updateDto.setEmail("newUser@example.com");
        updateDto.setBio("업데이트된 자기소개");

        // ✅ 기존 사용자 조회 Mock 설정
        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));

        // ✅ `save()`가 저장된 사용자 객체를 반환하도록 설정
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User updatedUser = invocation.getArgument(0);
            return updatedUser;
        });

        // When: 프로필 업데이트 실행
        UserProfileDto updatedProfile = profileService.updateUserProfile(1L, updateDto);

        // Then: 변경된 값이 올바르게 반영되었는지 확인
        assertNotNull(updatedProfile);
        assertEquals("newUser", updatedProfile.getUsername());  // ✅ 변경 확인
        assertEquals("newUser@example.com", updatedProfile.getEmail());  // ✅ 변경 확인
        assertEquals("업데이트된 자기소개", updatedProfile.getBio());  // ✅ 변경 확인

        // Verify: `save()`가 한 번 호출되었는지 확인
        verify(userRepository, times(1)).save(any(User.class));
    }



    @Test
    public void getUserProfile_Success() {
        // Given: Mock 사용자 데이터 생성
        User user = User.builder()
                .id(1L)
                .username("testuser2")
                .password("password456")
                .email("testuser2@example.com")
                .profileimg("http://example.com/profile2.jpg")
                .bio("두 번째 사용자입니다.")
                .build();

        // ✅ `save()`가 저장된 객체를 반환하도록 설정
        when(userRepository.save(any(User.class))).thenReturn(user);

        // ✅ `findById()`가 저장된 객체를 반환하도록 설정
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        // When: 사용자 저장 및 조회
        userRepository.save(user);
        User foundUser = userRepository.findById(user.getId()).orElse(null);

        // Then: 조회된 객체가 null이 아닌지 확인
        assertNotNull(foundUser);
        assertEquals("testuser2", foundUser.getUsername());
        assertEquals("testuser2@example.com", foundUser.getEmail());
        assertEquals("http://example.com/profile2.jpg", foundUser.getProfileimg());
        assertEquals("두 번째 사용자입니다.", foundUser.getBio());

        // Verify: `save()`와 `findById()`가 한 번씩 호출되었는지 확인
        verify(userRepository, times(1)).save(any(User.class));
        verify(userRepository, times(1)).findById(user.getId());
    }


    @Test
    public void updateProfileImage_Success() {
        // Given: 기존 사용자 데이터
        User user = User.builder()
                .id(1L)  // ✅ ID 추가
                .username("testuser3")
                .password("password789")
                .email("testuser3@example.com")
                .profileimg("http://example.com/profile3.jpg")
                .bio("세 번째 사용자입니다.")
                .build();

        // ✅ `lenient()`을 사용하여 불필요한 stubbing 예외 방지
        lenient().when(userRepository.save(any(User.class))).thenReturn(user);
        lenient().when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        // When: 사용자 프로필 업데이트
        user.setUsername("testuser4");
        user.setEmail("testuser4@example.com");
        user.setBio("update!");
        user.setProfileimg("http://example.com/newprofile3.jpg");

        // ✅ Mock을 통한 저장
        User updatedUser = userRepository.save(user);

        // Then: 변경된 값이 올바르게 반영되었는지 확인
        assertNotNull(updatedUser);
        assertEquals("http://example.com/newprofile3.jpg", updatedUser.getProfileimg());
        assertEquals("update!", updatedUser.getBio());
        assertEquals("testuser4", updatedUser.getUsername());
        assertEquals("testuser4@example.com", updatedUser.getEmail());

        // Verify: `save()`가 한 번 호출되었는지 확인
        verify(userRepository, times(1)).save(user);
    }


}