package com.remu.domain.notification.scheduler;

import com.remu.domain.galaxy.entity.Galaxy;
import com.remu.domain.galaxy.repository.GalaxyRepository;
import com.remu.domain.notification.enums.NotificationType;
import com.remu.domain.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationScheduler {

    private final GalaxyRepository galaxyRepository;
    private final NotificationService notificationService;

    // 1. 여행 시작 알림 (매일 09:00)
    @Scheduled(cron = "0 0 9 * * *")
    public void sendArrivalNotification() {
        LocalDate today = LocalDate.now();
        // 여행 시작일인 은하 조회
        List<Galaxy> galaxies = galaxyRepository.findAllByStartDate(today);

        log.info("Sending ARRIVAL notifications for {} galaxies", galaxies.size());
        for (Galaxy galaxy : galaxies) {
            notificationService.createAndSendNotification(galaxy.getUser(), galaxy, NotificationType.ARRIVAL);
        }
    }

    // 2. 기록 유도 알림 (매일 23:00)
    // 조건: 오늘 기록 0개일 때만 (Service에서 체크)
    @Scheduled(cron = "0 0 23 * * *")
    public void sendRecordNotification() {
        LocalDate today = LocalDate.now();
        // 여행 기간 중인 모든 은하 대상 (시작일 포함)
        List<Galaxy> galaxies = galaxyRepository.findAllByDateBetween(today);

        log.info("Sending RECORD notifications for {} galaxies", galaxies.size());
        for (Galaxy galaxy : galaxies) {
            notificationService.createAndSendNotification(galaxy.getUser(), galaxy, NotificationType.RECORD);
        }
    }

    // 3. 랜덤 질문 알림 (매일 15:00, 20:00)
    // 조건: 2일차부터 발송, 별 개수에 따라 난이도 다름 (Service에서 체크)
    @Scheduled(cron = "0 0 15 * * *")
    public void sendQuestionNotificationAfterLunch() {
        sendQuestionNotification();
    }

    @Scheduled(cron = "0 0 20 * * *")
    public void sendQuestionNotificationAfterDinner() {
        sendQuestionNotification();
    }

    private void sendQuestionNotification() {
        LocalDate today = LocalDate.now();
        List<Galaxy> galaxies = galaxyRepository.findAllByDateBetween(today);

        log.info("Sending QUESTION notifications for {} galaxies", galaxies.size());
        for (Galaxy galaxy : galaxies) {
            notificationService.createAndSendNotification(galaxy.getUser(), galaxy, NotificationType.QUESTION);
        }
    }

    // 4. 회고 알림 (매일 10:00)
    @Scheduled(cron = "0 0 10 * * *")
    public void sendReviewNotification() {
        LocalDate yesterday = LocalDate.now().minusDays(1); // 어제가 종료일인 경우
        List<Galaxy> galaxies = galaxyRepository.findAllByEndDate(yesterday);

        log.info("Sending REVIEW notifications for {} galaxies", galaxies.size());
        for (Galaxy galaxy : galaxies) {
            notificationService.createAndSendNotification(galaxy.getUser(), galaxy, NotificationType.REVIEW);
        }
    }
}