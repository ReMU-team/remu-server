package com.remu.domain.notification.service;

import com.remu.domain.galaxy.entity.Galaxy;
import com.remu.domain.notification.entity.Notification;
import com.remu.domain.notification.entity.Question;
import com.remu.domain.notification.enums.NotificationType;
import com.remu.domain.notification.repository.NotificationRepository;
import com.remu.domain.notification.repository.QuestionRepository;
import com.remu.domain.user.entity.User;
import com.remu.global.fcm.service.FcmService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final QuestionRepository questionRepository;
    private final FcmService fcmService; // FCM 서비스 연결

    // 알림 생성 및 전송 (내부 호출용)
    @Transactional
    public void createAndSendNotification(User user, Galaxy galaxy, NotificationType type) {
        String content = "";
        Question question = null;

        switch (type) {
            case ARRIVAL:
                content = "드디어 여행 날이에요! 즐거운 여행 되세요 ✈️";
                break;
            case RECORD:
                content = "여행지에 잘 도착하셨나요? 첫 기록을 남겨보세요! 📝";
                break;
            case QUESTION:
                // 랜덤 질문 조회
                question = questionRepository.findRandomQuestion()
                        .orElse(Question.builder().content("오늘 가장 기억에 남는 순간은?").build()); // 기본 질문
                content = question.getContent();
                break;
            case REVIEW:
                content = "여행은 즐거우셨나요? 여행의 추억을 정리해보세요 📚";
                break;
        }

        // 1. DB 저장
        Notification notification = Notification.builder()
                .user(user)
                .galaxy(galaxy)
                .type(type)
                .content(content)
                .question(question)
                .isRead(false)
                .build();
        
        notificationRepository.save(notification);

        // 2. FCM 전송 (비동기로 처리하면 좋음)
        fcmService.sendMessage(user.getFcmToken(), "ReMU", content);
    }

    // 유저의 알림 목록 조회
    public List<Notification> getNotifications(Long userId) {
        return notificationRepository.findAllByUserIdOrderByCreatedAtDesc(userId);
    }

    // 알림 읽음 처리
    @Transactional
    public void readNotification(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new IllegalArgumentException("알림을 찾을 수 없습니다."));
        notification.read(); // 읽음 처리
    }
}