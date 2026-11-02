package com.remu.global.auth.strategy;

import com.remu.domain.user.enums.SocialType;

public interface SocialLoginStrategy {

    SocialType supports();

    SocialUserInfo verify(String token);
}
