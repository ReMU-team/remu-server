package com.remu.global.auth.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.remu.global.auth.dto.ApplePublicKeyResponse;
import com.remu.global.auth.exception.AuthException;
import com.remu.global.auth.exception.code.AuthErrorCode;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AppleAuthService {

    private final ObjectMapper objectMapper;

    public Map<String, String> getAppleUserInfo(String identityToken) {
        try {
            // 1. identityToken의 헤더를 디코딩하여 kid와 alg 추출
            String headerJson = new String(Base64.getDecoder().decode(identityToken.split("\\.")[0]));
            Map<String, String> header = objectMapper.readValue(headerJson, Map.class);

            // 2. 애플 공개키 목록 가져오기
            ApplePublicKeyResponse response = new RestTemplate().getForObject(
                    "https://appleid.apple.com/auth/keys",
                    ApplePublicKeyResponse.class
            );

            // 3. 토큰의 헤더와 일치하는 공개키 찾기
            ApplePublicKeyResponse.Key appleKey = response.getMatchedKey(header.get("kid"), header.get("alg"));

            // 4. RSA 공개키 생성
            byte[] nBytes = Base64.getUrlDecoder().decode(appleKey.n());
            byte[] eBytes = Base64.getUrlDecoder().decode(appleKey.e());

            RSAPublicKeySpec publicKeySpec = new RSAPublicKeySpec(
                    new BigInteger(1, nBytes),
                    new BigInteger(1, eBytes)
            );
            KeyFactory keyFactory = KeyFactory.getInstance(appleKey.kty());
            PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);

            // 5. 서명 검증 및 페이로드 추출
            Claims claims = Jwts.parser()
                    .verifyWith(publicKey)
                    .build()
                    .parseSignedClaims(identityToken)
                    .getPayload();

            // 6. 결과 반환
            return Map.of(
                    "sub", claims.getSubject(),
                    "email", claims.get("email", String.class) != null ? claims.get("email", String.class) : ""
            );

        } catch (Exception e) {
            throw new AuthException(AuthErrorCode.INVALID_APPLE_TOKEN);
        }
    }
}