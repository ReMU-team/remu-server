package com.remu.global.auth.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.remu.global.auth.exception.AuthException;
import com.remu.global.auth.exception.code.AuthErrorCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;

@Service
public class GoogleAuthService {

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String googleClientId;

    public Map<String, Object> verifyGoogleToken(String idTokenString) throws Exception {
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new GsonFactory())
                .setAudience(Collections.singletonList(googleClientId))
                .build();

        try {
            GoogleIdToken idToken = verifier.verify(idTokenString);

            if (idToken != null) {
                Payload payload = idToken.getPayload();

                // 구글 페이로드에서 정보 추출
                return Map.of(
                        "id", payload.getSubject(),
                        "email", payload.getEmail(),
                        "nickname", payload.get("name") != null ? payload.get("name") : "구글 사용자",
                        "profile_image", payload.get("picture") != null ? payload.get("picture") : ""
                );
            } else {
                throw new AuthException(AuthErrorCode.INVALID_GOOGLE_TOKEN);
            }
        } catch (Exception e) {
            throw new AuthException(AuthErrorCode.GOOGLE_SERVER_ERROR);
        }
    }
}