package com.remu.global.auth.dto;

public class AuthReqDTO {
    public record RefreshDTO(String refreshToken) {}
    public record LogoutDTO(String refreshToken) {}
}
