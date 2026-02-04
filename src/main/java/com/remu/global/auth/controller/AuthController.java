package com.remu.global.auth.controller;

import com.remu.domain.user.enums.SocialType;
import com.remu.global.auth.dto.AuthReqDTO;
import com.remu.domain.user.entity.User;
import com.remu.domain.user.service.UserService;
import com.remu.global.apiPayload.ApiResponse;
import com.remu.global.auth.dto.AuthResDTO;
import com.remu.global.auth.exception.code.AuthSuccessCode;
import com.remu.global.auth.service.AuthService;
import com.remu.global.auth.service.GoogleAuthService;
import com.remu.global.auth.service.KakaoAuthService;
import com.remu.global.config.sercurity.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController implements AuthControllerDocs{

    private final AuthService authService;
    private final KakaoAuthService kakaoAuthService;
    private final GoogleAuthService googleAuthService;
    private final JwtTokenProvider tokenProvider;
    private final UserService userService;

    @PostMapping("/login/{provider}")
    public ResponseEntity<?> login(
            @PathVariable String provider,
            @RequestBody AuthReqDTO.SocialLoginDTO request
    ) throws Exception {
        String socialId;
        SocialType socialType;
        String email = null;
        String nickname = null;
        String profileImage = null;

        if ("kakao".equalsIgnoreCase(provider)) {
            socialType = SocialType.KAKAO;
            Map<String, Object> userInfo = kakaoAuthService.getKakaoUserInfo(request.token());

            socialId = (String) userInfo.get("id");
            email = (String) userInfo.get("email"); // 카카오에서 가져온 이메일
            nickname = (String) userInfo.get("nickname");
            profileImage = (String) userInfo.get("profile_image");

        } else if ("google".equalsIgnoreCase(provider)) {
            socialType = SocialType.GOOGLE;
            //TODO 구글도 나중에 KakaoAuthService처럼 Map을 반환하게 고쳐서 email, name을 받아오기
            socialId = googleAuthService.verifyGoogleToken(request.token());

        } else if ("apple".equalsIgnoreCase(provider)) {
            socialType = SocialType.APPLE;
            socialId = "apple_test_id";

        } else {
            throw new RuntimeException("지원하지 않는 플랫폼입니다.");
        }

        // 2. 통합된 유저 생성/조회
        User user = userService.findOrCreateUser(socialId, socialType, email, nickname, profileImage);

        // 3. 토큰 발행
        String ourAccessToken = tokenProvider.createAccessToken(user.getId(), user.getRole());
        String ourRefreshToken = tokenProvider.createRefreshToken(user.getId());

        return ResponseEntity.ok(ApiResponse.onSuccess(
                AuthSuccessCode.LOGIN_SUCCESS,
                new AuthResDTO.TokenDTO(ourAccessToken, ourRefreshToken)
        ));
    }

    // TODO refresh, logout 수정하기
    @PostMapping("/refresh")
    public ApiResponse<AuthResDTO.TokenDTO> refresh(
            @RequestBody AuthReqDTO.RefreshDTO request
    ) {
        AuthResDTO.TokenDTO tokenDTO = authService.refresh(request.refreshToken());
        return ApiResponse.onSuccess(AuthSuccessCode.TOKEN_REISSUE_SUCCESS, tokenDTO);
    }

    @PostMapping("/logout")
    public ApiResponse<Void> logout(
            @AuthenticationPrincipal(expression = "id") Long userId,
            @RequestBody AuthReqDTO.LogoutDTO request
    ) {
        authService.logout(userId, request.refreshToken());
        return ApiResponse.onSuccess(AuthSuccessCode.LOGOUT_SUCCESS, null);
    }

    @Override
    @GetMapping("/login/google")
    public void googleLoginInfo(){}

    @Override
    @GetMapping("/login/kakao")
    public void kakaoLoginInfo(){}


}
