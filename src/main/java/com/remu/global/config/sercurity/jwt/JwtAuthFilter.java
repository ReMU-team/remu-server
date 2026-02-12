package com.remu.global.config.sercurity.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.remu.global.apiPayload.ApiResponse;
import com.remu.global.apiPayload.code.BaseErrorCode;
import com.remu.global.auth.exception.AuthException;
import com.remu.global.auth.exception.code.AuthErrorCode;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final ObjectMapper objectMapper;

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        // 1. Request Header에서 토큰 추출
        String jwt = resolveToken(request);

        // 2. 토큰 유효성 검사 및 인증 객체 설정
        try {
            if (!StringUtils.hasText(jwt)) {
                filterChain.doFilter(request, response);
                return;
            }

            jwtTokenProvider.validateAccessTokenOrThrow(jwt);
            Authentication authentication = jwtTokenProvider.getAuthentication(jwt);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            filterChain.doFilter(request, response);

        } catch (AuthException e) {
            SecurityContextHolder.clearContext();
            sendErrorResponse(response, e.getCode());
        } catch (Exception e) {
            SecurityContextHolder.clearContext();
            log.error("JWT 처리 중 오류 발생: {}", e.getMessage());

            sendErrorResponse(response, AuthErrorCode.ACCESS_TOKEN_INVALID);
        }
    }

    private void sendErrorResponse(HttpServletResponse response, BaseErrorCode errorCode) throws IOException {
        response.setStatus(errorCode.getStatus().value());
        response.setContentType("application/json;charset=UTF-8");

        // ApiResponse.onFailure를 사용하여 규격 통일
        ApiResponse<Object> errorBody = ApiResponse.onFailure(errorCode, null);

        response.getWriter().write(objectMapper.writeValueAsString(errorBody));
    }

    // Request Header에서 토큰 정보를 꺼내오는 메서드
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(BEARER_PREFIX.length());
        }
        return null;
    }
}
