package com.remu.domain.notification.entity;

import com.remu.domain.galaxy.entity.Galaxy;
import com.remu.domain.notification.enums.NotificationType;
import com.remu.domain.user.entity.User;
import com.remu.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "notification")
public class Notification extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "galaxy_id", nullable = false)
    private Galaxy galaxy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id")
    private Question question; // 랜덤 질문일 경우에만 존재

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private NotificationType type;

    @Column(name = "content", nullable = false)
    private String content; // 알림 메시지 내용

    @Column(name = "is_read", nullable = false)
    @Builder.Default
    private Boolean isRead = false; // 읽음 여부

    // 읽음 처리 메서드
    public void read() {
        this.isRead = true;
    }
}