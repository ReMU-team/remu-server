package com.remu.global.config.sercurity.oauth.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.remu.domain.user.enums.Role;
import com.remu.global.auth.service.AuthService;
import com.remu.global.config.sercurity.jwt.JwtTokenProvider;
import com.remu.global.config.sercurity.oauth.UserPrincipal;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;
    private final AuthService authService;
    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        // 1. UserService에서 넘겨준 UserPrincipal 꺼내기
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        Long userId = principal.getId();
        String authority = authentication.getAuthorities().iterator().next().getAuthority();
        Role role = Role.fromKey(authority);

        // 2. 토큰 생성 및 저장
        String accessToken = jwtTokenProvider.createAccessToken(userId, role);
        String refreshToken = jwtTokenProvider.createRefreshToken(userId);

        authService.saveRefreshToken(
                userId,
                refreshToken,
                jwtTokenProvider.getExpiryDateTime(refreshToken)
        );

        // 3. JSON 응답 설정
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_OK);

        // 4. request body 구성
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("isSuccess", true);
        responseBody.put("code", "COMMON200");
        responseBody.put("message", "소셜 로그인 성공");

        Map<String, String> result = new HashMap<>();
        result.put("accessToken", accessToken);
        result.put("refreshToken", refreshToken);
        responseBody.put("result", result);

        // 5. JSON 출력
        String jsonResponse = objectMapper.writeValueAsString(responseBody);
        response.getWriter().write(jsonResponse);
        response.getWriter().flush(); // 스트림 비우기
    }
}
