package com.goorm.clonestagram.post.repository;

import com.goorm.clonestagram.post.domain.Posts;
import com.goorm.clonestagram.post.domain.SoftDelete;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface SoftDeleteRepository extends JpaRepository<SoftDelete, Long> {
    List<SoftDelete> findByDeletedAtBefore(LocalDateTime threshold);

    @Query("SELECT p FROM Posts p " +
            "WHERE p.user.id = :userId " +
            "AND p.id NOT IN (SELECT s.entityId FROM SoftDelete s WHERE s.entityType = 'POST') " +
            "ORDER BY p.createdAt DESC")
    Page<Posts> findAllByUserIdAndNotSoftDeleted(@Param("userId") Long userId, Pageable pageable);
}
