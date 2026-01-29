package com.remu.global.auth.controller;

import com.remu.global.apiPayload.ApiResponse;
import com.remu.global.auth.dto.AuthReqDTO;
import com.remu.global.auth.dto.AuthResDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Auth", description = "인증(Auth) 관련 API")
public interface AuthControllerDocs {
    @Operation(
            summary = "Access Token 재발급",
            description = """
                만료된 Access Token을 Refresh Token으로 재발급합니다.

                - Refresh Token은 요청 Body로 전달됩니다.
                - Refresh Token이 유효하지 않거나 만료된 경우 재발급에 실패합니다.
                - 재발급 성공 시 새로운 Access / Refresh Token을 반환합니다. (Refresh Rotation)
                """
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "토큰 재발급 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Refresh Token이 유효하지 않거나 만료됨"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "유저를 찾을 수 없음")
    })
    @PostMapping("/api/v1/auth/refresh")
    ApiResponse<AuthResDTO.TokenDTO> refresh(
            @RequestBody AuthReqDTO.RefreshDTO request
    );

    @Operation(
            summary = "로그아웃",
            description = """
                Refresh Token을 폐기하여 로그아웃 처리합니다.

                - Refresh Token은 요청 Body로 전달됩니다.
                - 이미 로그아웃된 상태여도 성공을 반환합니다. (Idempotent)
                """
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "로그아웃 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Refresh Token 형식 오류")
    })
    @PostMapping("/api/v1/auth/logout")
    ApiResponse<Void> logout(
            @RequestBody AuthReqDTO.LogoutDTO request
    );
}
