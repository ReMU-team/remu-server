package com.remu.global.auth.service;

import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Service;

@Service
public class AppleAuthService {

    public String verifyAppleToken(String identityToken) {
        try {
            // TODO 실제 운영 환경에서는 애플의 공개키를 가져와서 서명(Signature)을 검증해야 함
            // 우선 'sub'(사용자 고유 식별자)를 파싱하는 로직부터 연결
            return Jwts.parser()
                    .build()
                    .parseUnsecuredClaims(identityToken) // 검증 라이브러리 추가 전까지 임시 사용
                    .getPayload()
                    .getSubject();
        } catch (Exception e) {
            throw new RuntimeException("유효하지 않은 애플 토큰입니다.");
        }
    }
}