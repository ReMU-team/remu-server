package com.remu.domain.notification.service;

import com.remu.domain.galaxy.entity.Galaxy;
import com.remu.domain.notification.entity.Notification;
import com.remu.domain.notification.entity.Question;
import com.remu.domain.notification.enums.NotificationType;
import com.remu.domain.notification.enums.QuestionDifficulty;
import com.remu.domain.notification.repository.NotificationRepository;
import com.remu.domain.notification.repository.QuestionRepository;
import com.remu.domain.star.repository.StarRepository;
import com.remu.domain.user.entity.User;
import com.remu.global.fcm.service.FcmService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final QuestionRepository questionRepository;
    private final StarRepository starRepository;
    private final FcmService fcmService;

    // 알림 생성 및 전송 (내부 호출용)
    @Transactional
    public void createAndSendNotification(User user, Galaxy galaxy, NotificationType type) {
        // 1. 알림 수신 거부한 유저는 건너뜀
        if (!user.getIsAlarmOn()) {
            return;
        }

        // 2. 오늘 작성한 별 개수 확인
        LocalDate today = LocalDate.now();
        Long starCount = starRepository.countByGalaxyIdAndRecordDate(galaxy.getId(), today);

        String content = "";
        Question question = null;

        switch (type) {
            case ARRIVAL:
                content = "드디어 여행 날이에요! 즐거운 여행 되세요 ✈️";
                break;

            case RECORD:
                // 여행 첫날인지 확인
                if (today.isEqual(galaxy.getArrivalDate())) {
                    // 첫날은 기록 여부 상관없이 무조건 발송
                    content = "여행지에 잘 도착하셨나요? 첫 기록을 남겨보세요! 📝";
                } else {
                    // 그 외 날짜는 오늘 기록이 없을 때만 발송
                    if (starCount > 0) {
                        return;
                    }
                    content = "오늘 하루는 어떠셨나요? 기록을 남겨보세요.";
                }
                break;

            case QUESTION:
                // 별 개수에 따라 난이도 결정
                QuestionDifficulty difficulty = (starCount == 0) ? QuestionDifficulty.EASY : QuestionDifficulty.HARD;
                
                question = questionRepository.findRandomQuestionByDifficulty(difficulty)
                        .orElse(Question.builder().content("오늘 가장 기억에 남는 순간은?").build());
                content = question.getContent();
                break;

            case REVIEW:
                content = "여행은 즐거우셨나요? 여행의 추억을 정리해보세요 📚";
                break;
        }

        // 3. DB 저장
        Notification notification = Notification.builder()
                .user(user)
                .galaxy(galaxy)
                .type(type)
                .content(content)
                .question(question)
                .isRead(false)
                .build();
        
        notificationRepository.save(notification);

        // 4. FCM 전송
        fcmService.sendMessage(user.getFcmToken(), "ReMU", content);
    }
}