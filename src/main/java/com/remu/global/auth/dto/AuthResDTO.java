package com.remu.global.auth.dto;

public class AuthResDTO {
    public record TokenDTO(String accessToken, String refreshToken) {}
}
