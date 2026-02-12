package com.remu.global.auth.dto;

import lombok.Builder;

public class AuthResDTO {
    @Builder
    public record Token(String accessToken, String refreshToken, Boolean isNewUser ) {}
    @Builder
    public record TokenDTO(String accessToken, String refreshToken) {}

}
