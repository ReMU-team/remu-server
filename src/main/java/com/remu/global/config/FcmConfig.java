package com.remu.global.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Slf4j
@Configuration
public class FcmConfig {

    @PostConstruct
    public void init() {
        try {
            // 이미 초기화되어 있다면 건너뜀 (중복 초기화 방지)
            List<FirebaseApp> apps = FirebaseApp.getApps();
            if (apps != null && !apps.isEmpty()) {
                return;
            }

            InputStream inputStream = new ClassPathResource("firebase/serviceAccountKey.json").getInputStream();
            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(inputStream))
                    .build();

            FirebaseApp.initializeApp(options);
            log.info("Firebase Application Initialized");

        } catch (IOException e) {
            log.error("Firebase Initialize Error", e);
            throw new RuntimeException("Firebase 초기화 실패", e);
        }
    }
}