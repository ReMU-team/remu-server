package com.remu.global.auth.strategy;

import com.remu.domain.user.enums.SocialType;
import com.remu.global.auth.exception.AuthException;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SocialLoginStrategyResolverTest {

    @Test
    void provider에_맞는_전략을_반환한다() {
        SocialLoginStrategy kakaoStrategy = mock(SocialLoginStrategy.class);
        SocialLoginStrategy googleStrategy = mock(SocialLoginStrategy.class);
        when(kakaoStrategy.supports()).thenReturn(SocialType.KAKAO);
        when(googleStrategy.supports()).thenReturn(SocialType.GOOGLE);

        SocialLoginStrategyResolver resolver =
                new SocialLoginStrategyResolver(List.of(kakaoStrategy, googleStrategy));

        assertThat(resolver.resolve("kakao")).isSameAs(kakaoStrategy);
        assertThat(resolver.resolve("GOOGLE")).isSameAs(googleStrategy);
    }

    @Test
    void 지원하지_않는_provider는_예외가_발생한다() {
        SocialLoginStrategyResolver resolver = new SocialLoginStrategyResolver(List.of());

        assertThatThrownBy(() -> resolver.resolve("naver"))
                .isInstanceOf(AuthException.class);
    }

    @Test
    void 같은_provider_전략이_중복되면_애플리케이션_시작에_실패한다() {
        SocialLoginStrategy first = mock(SocialLoginStrategy.class);
        SocialLoginStrategy second = mock(SocialLoginStrategy.class);
        when(first.supports()).thenReturn(SocialType.KAKAO);
        when(second.supports()).thenReturn(SocialType.KAKAO);

        assertThatThrownBy(() -> new SocialLoginStrategyResolver(List.of(first, second)))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("KAKAO");
    }
}
