package com.remu.global.auth.strategy;

import com.remu.domain.user.enums.SocialType;
import com.remu.global.auth.exception.AuthException;
import com.remu.global.auth.exception.code.AuthErrorCode;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Component
public class SocialLoginStrategyResolver {

    private final Map<SocialType, SocialLoginStrategy> strategies;

    public SocialLoginStrategyResolver(List<SocialLoginStrategy> strategies) {
        EnumMap<SocialType, SocialLoginStrategy> strategyMap = new EnumMap<>(SocialType.class);

        for (SocialLoginStrategy strategy : strategies) {
            SocialLoginStrategy previous = strategyMap.put(strategy.supports(), strategy);
            if (previous != null) {
                throw new IllegalStateException("Duplicate social login strategy: " + strategy.supports());
            }
        }

        this.strategies = Map.copyOf(strategyMap);
    }

    public SocialLoginStrategy resolve(String provider) {
        SocialType socialType = parseSocialType(provider);
        SocialLoginStrategy strategy = strategies.get(socialType);

        if (strategy == null) {
            throw new AuthException(AuthErrorCode.INVALID_PROVIDER);
        }

        return strategy;
    }

    private SocialType parseSocialType(String provider) {
        if (provider == null || provider.isBlank()) {
            throw new AuthException(AuthErrorCode.INVALID_PROVIDER);
        }

        try {
            return SocialType.valueOf(provider.toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException exception) {
            throw new AuthException(AuthErrorCode.INVALID_PROVIDER);
        }
    }
}
