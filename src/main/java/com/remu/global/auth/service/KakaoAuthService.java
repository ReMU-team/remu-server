package com.remu.global.auth.service;

import com.remu.global.auth.exception.AuthException;
import com.remu.global.auth.exception.code.AuthErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Slf4j
@Service
public class KakaoAuthService {

    @Value("${kakao.admin-key")
    private String adminKey;

    public Map<String, Object> getKakaoUserInfo(String accessToken) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        String url = "https://kapi.kakao.com/v2/user/me";

        try {
            ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);
            Map<String, Object> body = response.getBody();

            if (body == null) {
                throw new AuthException(AuthErrorCode.KAKAO_SERVER_ERROR);
            }

            // 계정 정보 및 프로필 추출
            Map<String, Object> kakaoAccount = (Map<String, Object>) body.get("kakao_account");
            Map<String, Object> properties = (Map<String, Object>) body.get("properties");

            // 닉네임, 이미지 null 대비
            String nickname = (properties != null && properties.get("nickname") != null)
                    ? properties.get("nickname").toString() : "사용자";
            String profileImage = (properties != null && properties.get("profile_image") != null)
                    ? properties.get("profile_image").toString() : "";

            // 이메일 추출
            String email = (kakaoAccount != null && kakaoAccount.get("email") != null)
                    ? kakaoAccount.get("email").toString() : "no-email@kakao.com";

            return Map.of(
                    "id", body.get("id").toString(),
                    "email", email,
                    "nickname", nickname,
                    "profile_image", profileImage
            );

        } catch (HttpClientErrorException e) {
            // 카카오에서 에러를 보낸 경우 (토큰 만료, 잘못된 토큰 등)
            log.error("카카오 토큰 검증 실패: {}", e.getResponseBodyAsString());
            throw new AuthException(AuthErrorCode.INVALID_KAKAO_TOKEN);
        } catch (Exception e) {
            // 연결 시간 초과 등 기타 서버 에러
            log.error("카카오 API 호출 중 알 수 없는 오류 발생: {}", e.getMessage());
            throw new AuthException(AuthErrorCode.KAKAO_SERVER_ERROR);
        }
    }

    public void unlinkKakao(String socialId) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "KakaoAK" + adminKey); // 클라이언트에서 넘겨준 토큰 사용
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("target_id_type", "user_id");
        params.add("target_id", socialId);

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, headers);
        String url = "https://kapi.kakao.com/v1/user/unlink";

        try {
            restTemplate.exchange(url, HttpMethod.POST, entity, Map.class);
            log.info("카카오 unlink 성공. 소셜ID: {}", socialId);
        } catch (HttpClientErrorException e) {
            log.error("카카오 unlink 실패: {}", e.getResponseBodyAsString());
            throw new AuthException(AuthErrorCode.INVALID_KAKAO_TOKEN);
        } catch (Exception e) {
            log.error("카카오 unlink 중 서버 오류: {}", e.getMessage());
            throw new AuthException(AuthErrorCode.KAKAO_SERVER_ERROR);
        }
    }
}