package com.remu.global.config.sercurity.jwt;

import com.remu.domain.user.enums.Role;
import com.remu.global.auth.exception.AuthException;
import com.remu.global.auth.exception.code.AuthErrorCode;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.Date;

import javax.crypto.SecretKey;


@Component
@Getter
@Slf4j
public class JwtTokenProvider {

    private final SecretKey key;
    private final long accessTokenExpirationTime;
    private final long refreshTokenExpirationTime;

    public JwtTokenProvider(
            @Value("${jwt.secret}") String secretKey,
            @Value("${jwt.access-expiration}") long accessTokenExpirationTime,
            @Value("${jwt.refresh-expiration}") long refreshTokenExpirationTime
    ) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.accessTokenExpirationTime = accessTokenExpirationTime;
        this.refreshTokenExpirationTime = refreshTokenExpirationTime;
    }

    // Access Token 생성
    public String createAccessToken(Long userId, Role role) {
        Date now = new Date();
        Date exp = new Date(now.getTime() + accessTokenExpirationTime);

        return Jwts.builder()
                .subject(String.valueOf(userId))
                .claim("role", role.getKey())
                .issuedAt(now)
                .expiration(exp)
                .signWith(key, Jwts.SIG.HS256)
                .compact();
    }

    // Refresh Token 생성
    public String createRefreshToken(Long userId) {
        Date now = new Date();
        Date exp = new Date(now.getTime() + refreshTokenExpirationTime);

        return Jwts.builder()
                .subject(String.valueOf(userId))
                .issuedAt(now)
                .expiration(exp)
                .signWith(key, Jwts.SIG.HS256)
                .compact();
    }

    // 토큰 서명/만료 검증
    public boolean validateToken(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException exception) {
            log.warn("Invalid JWT token: {}", exception.getMessage());
            return false;
        }
    }

    // Access Token 검증
    public void validateAccessTokenOrThrow(String token) {
        try {
            parseClaims(token);
        } catch (ExpiredJwtException e) {
            throw new AuthException(AuthErrorCode.ACCESS_TOKEN_EXPIRED);
        } catch (JwtException | IllegalArgumentException e) {
            throw new AuthException(AuthErrorCode.ACCESS_TOKEN_INVALID);
        }
    }

    // 토큰에서 Claims 파싱
    private Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    // Access Token에서 userId 추출
    public Long getUserId(String token) {
        Claims claims = parseClaims(token);
        return Long.valueOf(claims.getSubject());
    }

    // Access Token에서 Role 추출
    public String getRole(String token) {
        Claims claims = parseClaims(token);
        return (String) claims.get("role");
    }

    // Spring Security Authentication 객체 생성
    public Authentication getAuthentication(String token) {
        Claims claims = parseClaims(token);

        Long userId = Long.valueOf(claims.getSubject());
        String role = (String) claims.get("role");

        var principal = new JwtPrincipal(userId, role);

        return new UsernamePasswordAuthenticationToken(
                principal,
                null,
                role == null
                        ? Collections.emptyList()
                        : Collections.singletonList(new SimpleGrantedAuthority(role))
        );
    }

    // AccessToken에서 만료 시간 추출
    public LocalDateTime getExpiryDateTime(String token) {
        Claims claims = parseClaims(token);
        Date exp = claims.getExpiration();
        return exp.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    // AccessToken에서 RoleKey 추출
    public Role getRoleEnum(String token) {
        String roleKey = getRole(token);
        return Role.fromKey(roleKey);
    }

}
