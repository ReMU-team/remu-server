package com.remu.domain.notification.repository;

import com.remu.domain.notification.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    // 특정 유저의 알림 목록 조회 (최신순)
    List<Notification> findAllByUserIdOrderByCreatedAtDesc(Long userId);
    
    // 특정 유저의 읽지 않은 알림 개수 조회
    long countByUserIdAndIsReadFalse(Long userId);
}