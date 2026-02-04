package com.remu.global.auth.service;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import org.springframework.http.HttpHeaders;
import java.util.Map;

@Service
public class KakaoAuthService {

    public Map<String, Object> getKakaoUserInfo(String accessToken) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        String url = "https://kapi.kakao.com/v2/user/me";
        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);

        Map<String, Object> body = response.getBody();

        // 카카오 상세 계정 정보에서 이메일 추출
        Map<String, Object> kakaoAccount = (Map<String, Object>) body.get("kakao_account");
        // 카카오 프로필 정보에서 닉네임, 사진 추출
        Map<String, Object> properties = (Map<String, Object>) body.get("properties");

        return Map.of(
                "id", body.get("id").toString(),
                "email", kakaoAccount.get("email") != null ? kakaoAccount.get("email") : "no-email@kakao.com",
                "nickname", properties.get("nickname"),
                "profile_image", properties.get("profile_image")
        );
    }
}
