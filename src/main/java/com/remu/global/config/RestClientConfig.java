package com.remu.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

import java.net.http.HttpClient;
import java.time.Duration;

@Configuration
public class RestClientConfig {

    @Bean
    public RestClient googleRestClient() {
        // 1. JDK HttpClient 자체에서 연결 타임아웃 설정
        HttpClient httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(5)) // 연결 타임아웃 5초
                .build();

        // 2. 위에서 만든 HttpClient를 사용하는 팩토리 생성
        JdkClientHttpRequestFactory factory = new JdkClientHttpRequestFactory();

        // 3. 읽기 타임아웃은 팩토리에서 직접 설정 가능
        // 응답 타임아웃 5초
        factory.setReadTimeout(Duration.ofSeconds(5));

        return RestClient.builder()
                .requestFactory(factory)
                .baseUrl("https://maps.googleapis.com")
                .build();

    }
}
