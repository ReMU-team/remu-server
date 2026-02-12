package com.remu.global.auth.dto;

public class AuthReqDTO {
    public record RefreshDTO(String refreshToken) {}
    public record LogoutDTO(String refreshToken) {}

    // 프론트에서 토큰을 보낼 때 사용할 DTO
    public record SocialLoginDTO(
            String token
    ) {}
}
