package com.remu.global.auth.service;

import com.remu.domain.user.enums.Role;
import com.remu.domain.user.exception.UserException;
import com.remu.domain.user.exception.code.UserErrorCode;
import com.remu.domain.user.repository.UserRepository;
import com.remu.global.auth.dto.AuthResDTO;
import com.remu.global.auth.entity.RefreshToken;
import com.remu.global.auth.exception.AuthException;
import com.remu.global.auth.exception.code.AuthErrorCode;
import com.remu.global.auth.repository.RefreshTokenRepository;
import com.remu.global.config.sercurity.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    public AuthResDTO.TokenDTO refresh(String refreshTokenRaw) {
        String hash = sha256(refreshTokenRaw);

        RefreshToken saved = refreshTokenRepository.findByTokenHash(hash)
                .orElseThrow(() -> new AuthException(AuthErrorCode.REFRESH_TOKEN_INVALID));

        if (saved.isExpired()) {
            refreshTokenRepository.delete(saved);
            throw new AuthException(AuthErrorCode.REFRESH_TOKEN_EXPIRED);
        }

        // refreshToken에서 userId 뽑기
        Long userId = jwtTokenProvider.getUserId(refreshTokenRaw);

        // DB에 저장된 userId와 토큰의 userId 일치 검증
        if (!saved.getUserId().equals(userId)) {
            refreshTokenRepository.delete(saved);
            throw new AuthException(AuthErrorCode.REFRESH_TOKEN_TAMPERED);
        }

        // access 새로 발급
        Role role = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND))
                .getRole();

        String newAccess = jwtTokenProvider.createAccessToken(userId, role);

        // refresh 로테이션 : 새 refresh 토큰 발급 + 기존 삭제 + 새로 저장
        refreshTokenRepository.delete(saved);

        String newRefresh = jwtTokenProvider.createRefreshToken(userId);
        refreshTokenRepository.save(
                RefreshToken.builder()
                        .userId(userId)
                        .tokenHash(sha256(newRefresh))
                        .expiresAt(jwtTokenProvider.getExpiryDateTime(newRefresh))
                        .build()
        );

        return new AuthResDTO.TokenDTO(newAccess, newRefresh);
    }

    public void logout(Long userId, String refreshTokenRaw) {
        String hash = sha256(refreshTokenRaw);

        // 1. 해시 토큰 검증
        RefreshToken saved = refreshTokenRepository.findByTokenHash(hash)
                .orElseThrow(() -> new AuthException(AuthErrorCode.REFRESH_TOKEN_INVALID));

        // 토큰의 주인과 현재 로그아웃 요청자가 일치하는지 검증
        if (!saved.getUserId().equals(userId)) {
            throw new AuthException(AuthErrorCode.REFRESH_TOKEN_TAMPERED);
        }

        // 2. 존재할 때만 삭제 수행
        refreshTokenRepository.delete(saved);
    }

    public void saveRefreshToken(Long userId, String refreshTokenRaw, LocalDateTime expiresAt) {
        refreshTokenRepository.deleteByUserId(userId);

        refreshTokenRepository.save(
                RefreshToken.builder()
                        .userId(userId)
                        .tokenHash(sha256(refreshTokenRaw))
                        .expiresAt(expiresAt)
                        .build()
        );
    }

    private String sha256(String raw) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashed = digest.digest(raw.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : hashed) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
    }
}
