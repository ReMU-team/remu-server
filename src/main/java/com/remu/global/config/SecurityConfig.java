package com.remu.global.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.remu.global.config.sercurity.jwt.JwtAuthFilter;
import com.remu.global.config.sercurity.jwt.JwtAuthenticationEntryPoint;
import com.remu.global.config.sercurity.jwt.JwtTokenProvider;
import com.remu.global.config.sercurity.oauth.CustomOAuth2UserService;
import com.remu.global.config.sercurity.oauth.handler.OAuth2SuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;    // JWT 토큰 생성/검증 담당
    private final CustomOAuth2UserService customOAuth2UserService;  // OAuth2 로그인 시 사용자 정보 로드
    private final OAuth2SuccessHandler oAuth2SuccessHandler;    // OAuth2 로그인 성공 후 처리 (JWT 발급)
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint; // 인증 실패 처리
    private final ObjectMapper objectMapper;

    // JWT 인증 필터
    @Bean
    public JwtAuthFilter jwtAuthFilter() {
        return new JwtAuthFilter(jwtTokenProvider, objectMapper);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // CSRF 및 기본 로그인 폼 비활성화
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)

                // JWT로 할거라 세션 사용X
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // 안중 실패(401) 응답 커스터마이징
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                )

                // 모든 API 경로를 인증 없이 허용
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/login/**", "/oauth2/**", "/swagger-ui/**", "/v3/api-docs/**", "/api/v1/auth/login/**", "/api/v1/auth/refresh").permitAll()
                        .requestMatchers("/api/**").authenticated()
                        .anyRequest().permitAll()
                )

                // OAuth2 로그인 설정
                .oauth2Login(oauth -> oauth
                        .userInfoEndpoint(userInfo ->
                                userInfo.userService(customOAuth2UserService)
                        )
                        .successHandler(oAuth2SuccessHandler)
                )

                // JWT 인증 필터 UsernamePasswordAuthenticationFilter 앞에 등록
                .addFilterBefore(jwtAuthFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
