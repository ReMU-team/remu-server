package com.remu.global.auth.controller;

import com.remu.domain.user.enums.SocialType;
import com.remu.global.auth.dto.AuthReqDTO;
import com.remu.domain.user.entity.User;
import com.remu.domain.user.service.UserService;
import com.remu.global.apiPayload.ApiResponse;
import com.remu.global.auth.dto.AuthResDTO;
import com.remu.global.auth.exception.AuthException;
import com.remu.global.auth.exception.code.AuthErrorCode;
import com.remu.global.auth.exception.code.AuthSuccessCode;
import com.remu.global.auth.service.AppleAuthService;
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
    private final AppleAuthService appleAuthService;
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

            Map<String, Object> userInfo = googleAuthService.verifyGoogleToken(request.token());

            socialId = (String) userInfo.get("id");
            email = (String) userInfo.get("email");
            nickname = (String) userInfo.get("nickname");
            profileImage = (String) userInfo.get("profile_image");

        } else if ("apple".equalsIgnoreCase(provider)) {
            socialType = SocialType.APPLE;

            // 애플 토큰 검증 및 정보 추출
            Map<String, String> appleUserInfo = appleAuthService.getAppleUserInfo(request.token());

            socialId = appleUserInfo.get("sub");
            email = appleUserInfo.get("email");

            nickname = "Apple User";
        } else {
            throw new AuthException(AuthErrorCode.INVALID_PROVIDER);
        }

        // 2. 통합된 유저 생성/조회
        UserService.UserResult result = userService.findOrCreateUser(socialId, socialType, email, nickname, profileImage);
        User user = result.user();
        boolean isNewUser = result.isNewUser();

        // 3. 토큰 발행
        String ourAccessToken = tokenProvider.createAccessToken(user.getId(), user.getRole());
        String ourRefreshToken = tokenProvider.createRefreshToken(user.getId());

        // 4. DB에 리프레시 토큰 저장
        authService.saveRefreshToken(
                user.getId(),
                ourRefreshToken,
                tokenProvider.getExpiryDateTime(ourRefreshToken)
        );

        // 5. 응답 (access token, refresh token, isNewUser)
        return ResponseEntity.ok(ApiResponse.onSuccess(
                AuthSuccessCode.LOGIN_SUCCESS,
                new AuthResDTO.Token(ourAccessToken, ourRefreshToken, isNewUser)
        ));
    }

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
