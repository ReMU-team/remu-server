package com.remu.global.auth.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.remu.domain.user.enums.SocialType;
import com.remu.global.auth.exception.AuthException;
import com.remu.global.auth.exception.code.AuthErrorCode;
import com.remu.global.auth.strategy.SocialLoginStrategy;
import com.remu.global.auth.strategy.SocialUserInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class GoogleAuthService implements SocialLoginStrategy {

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String googleClientId;

    @Override
    public SocialType supports() {
        return SocialType.GOOGLE;
    }

    @Override
    public SocialUserInfo verify(String idTokenString) {
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new GsonFactory())
                .setAudience(Collections.singletonList(googleClientId))
                .build();

        try {
            GoogleIdToken idToken = verifier.verify(idTokenString);

            if (idToken != null) {
                Payload payload = idToken.getPayload();

                // 구글 페이로드에서 정보 추출
                return new SocialUserInfo(
                        payload.getSubject(),
                        payload.getEmail(),
                        payload.get("name") != null ? payload.get("name").toString() : "구글 사용자",
                        payload.get("picture") != null ? payload.get("picture").toString() : ""
                );
            } else {
                throw new AuthException(AuthErrorCode.INVALID_GOOGLE_TOKEN);
            }
        } catch (Exception e) {
            throw new AuthException(AuthErrorCode.GOOGLE_SERVER_ERROR);
        }
    }
}
