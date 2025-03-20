package com.goorm.clonestagram.user.domain;


import com.goorm.clonestagram.common.base.BaseTimeEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;


/**
 * User 엔티티
 * User에 대한 매핑 정보를 담는 엔티티
 * 프로필 수정 및 설정 기능을 구현하기 위해 ERD에 맞춰 작성함
 */


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User extends BaseTimeEntity { // BaseTimeEntity를 상속받아 create, update 관리

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userId")
    private Long id;

    @Column(nullable = false, length = 20)
    private String username;

    @Column(nullable = false)
    private String password;

    @Email(message = "Invaild email format") // 유효성 검사
    @Column(nullable = false, unique = true)
    private String email;

    @Lob // 큰 데이터를 저장하기 위해 사용
    @Column(columnDefinition = "TEXT") // 이미지의 경로(url)을 받기 위함
    private String profileimg;

    @Column(columnDefinition = "TEXT")
    private String bio; // 자기소개글

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @CreationTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

     /*


    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Post> posts;

    @OneToMany(mappedBy = "follow테이블에서 이름가져오기", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Follow> following;

    @OneToMany(mappedBy = "follow테이블에서 가져오기", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Follow> followers;
     */

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }


}
