package com.remu.global.fcm.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class FcmService {

    public void sendMessage(String token, String title, String body) {
        if (token == null || token.isEmpty()) {
            log.warn("FCM Token is empty. Skip sending message.");
            return;
        }

        try {
            Notification notification = Notification.builder()
                    .setTitle(title)
                    .setBody(body)
                    .build();

            Message message = Message.builder()
                    .setToken(token)
                    .setNotification(notification)
                    .build();

            String response = FirebaseMessaging.getInstance().send(message);
            log.info("Successfully sent message: {}", response);

        } catch (Exception e) {
            log.error("Failed to send FCM message", e);
            // FCM 전송 실패가 전체 로직을 망치지 않도록 예외를 던지지 않고 로그만 남김
        }
    }
}