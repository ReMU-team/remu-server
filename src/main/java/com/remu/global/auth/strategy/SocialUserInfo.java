package com.remu.global.auth.strategy;

public record SocialUserInfo(
        String socialId,
        String email,
        String nickname,
        String profileImage
) {
}
