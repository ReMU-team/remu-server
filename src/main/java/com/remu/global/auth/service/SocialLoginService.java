package com.remu.global.auth.service;

import com.remu.domain.user.entity.User;
import com.remu.domain.user.service.UserService;
import com.remu.global.auth.dto.AuthResDTO;
import com.remu.global.auth.strategy.SocialLoginStrategy;
import com.remu.global.auth.strategy.SocialLoginStrategyResolver;
import com.remu.global.auth.strategy.SocialUserInfo;
import com.remu.global.config.sercurity.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SocialLoginService {

    private final SocialLoginStrategyResolver strategyResolver;
    private final UserService userService;
    private final JwtTokenProvider tokenProvider;
    private final AuthService authService;

    public AuthResDTO.Token login(String provider, String providerToken) {
        SocialLoginStrategy strategy = strategyResolver.resolve(provider);
        SocialUserInfo socialUserInfo = strategy.verify(providerToken);

        UserService.UserResult result = userService.findOrCreateUser(
                socialUserInfo.socialId(),
                strategy.supports(),
                socialUserInfo.email(),
                socialUserInfo.nickname(),
                socialUserInfo.profileImage()
        );
        User user = result.user();

        String accessToken = tokenProvider.createAccessToken(user.getId(), user.getRole());
        String refreshToken = tokenProvider.createRefreshToken(user.getId());

        authService.saveRefreshToken(
                user.getId(),
                refreshToken,
                tokenProvider.getExpiryDateTime(refreshToken)
        );

        return new AuthResDTO.Token(accessToken, refreshToken, result.isNewUser());
    }
}
