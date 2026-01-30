package com.remu.global.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AiConfig {

    @Bean
    ChatClient chatClient(ChatClient.Builder builder) {
        return builder
                .defaultSystem("너는 여행 기록 서비스 'ReMu'의 사용자 데이터를 기반으로 여행 피드백을 주는 가이드야." +
                        "유저의 데이터를 분석해서 따뜻한 말로 공감해주고 격려해줘.")
                .build();
    }
}
