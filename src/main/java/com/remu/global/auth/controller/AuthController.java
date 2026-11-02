package com.remu.global.auth.controller;

import com.remu.global.auth.dto.AuthReqDTO;
import com.remu.global.apiPayload.ApiResponse;
import com.remu.global.auth.dto.AuthResDTO;
import com.remu.global.auth.exception.code.AuthSuccessCode;
import com.remu.global.auth.service.AuthService;
import com.remu.global.auth.service.SocialLoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController implements AuthControllerDocs{

    private final AuthService authService;
    private final SocialLoginService socialLoginService;

    @Override
    @PostMapping("/login/{provider}")
    public ResponseEntity<ApiResponse<AuthResDTO.Token>> login(
            @PathVariable String provider,
            @RequestBody AuthReqDTO.SocialLoginDTO request
    ) {
        return ResponseEntity.ok(ApiResponse.onSuccess(
                AuthSuccessCode.LOGIN_SUCCESS,
                socialLoginService.login(provider, request.token())
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
