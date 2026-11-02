package com.remu.global.auth.service;

import com.remu.domain.user.entity.User;
import com.remu.domain.user.enums.Role;
import com.remu.domain.user.enums.SocialType;
import com.remu.domain.user.service.UserService;
import com.remu.global.auth.dto.AuthResDTO;
import com.remu.global.auth.strategy.SocialLoginStrategy;
import com.remu.global.auth.strategy.SocialLoginStrategyResolver;
import com.remu.global.auth.strategy.SocialUserInfo;
import com.remu.global.config.sercurity.jwt.JwtTokenProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SocialLoginServiceTest {

    @Mock
    private SocialLoginStrategyResolver strategyResolver;

    @Mock
    private UserService userService;

    @Mock
    private JwtTokenProvider tokenProvider;

    @Mock
    private AuthService authService;

    @Mock
    private SocialLoginStrategy strategy;

    @Mock
    private User user;

    @InjectMocks
    private SocialLoginService socialLoginService;

    @Test
    void 선택된_전략으로_사용자를_검증하고_ReMU_토큰을_발급한다() {
        String providerToken = "provider-token";
        String refreshToken = "refresh-token";
        LocalDateTime refreshTokenExpiry = LocalDateTime.now().plusDays(14);
        SocialUserInfo socialUserInfo = new SocialUserInfo(
                "social-id",
                "user@example.com",
                "사용자",
                "profile-image"
        );

        when(strategyResolver.resolve("kakao")).thenReturn(strategy);
        when(strategy.supports()).thenReturn(SocialType.KAKAO);
        when(strategy.verify(providerToken)).thenReturn(socialUserInfo);
        when(userService.findOrCreateUser(
                socialUserInfo.socialId(),
                SocialType.KAKAO,
                socialUserInfo.email(),
                socialUserInfo.nickname(),
                socialUserInfo.profileImage()
        )).thenReturn(new UserService.UserResult(user, true));
        when(user.getId()).thenReturn(1L);
        when(user.getRole()).thenReturn(Role.USER);
        when(tokenProvider.createAccessToken(1L, Role.USER)).thenReturn("access-token");
        when(tokenProvider.createRefreshToken(1L)).thenReturn(refreshToken);
        when(tokenProvider.getExpiryDateTime(refreshToken)).thenReturn(refreshTokenExpiry);

        AuthResDTO.Token result = socialLoginService.login("kakao", providerToken);

        assertThat(result.accessToken()).isEqualTo("access-token");
        assertThat(result.refreshToken()).isEqualTo(refreshToken);
        assertThat(result.isNewUser()).isTrue();
        verify(authService).saveRefreshToken(1L, refreshToken, refreshTokenExpiry);
    }
}
