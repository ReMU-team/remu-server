package com.remu.global.auth.dto;

import com.remu.global.auth.exception.AuthException;
import com.remu.global.auth.exception.code.AuthErrorCode;

import java.util.List;

public record ApplePublicKeyResponse(List<Key> keys) {
    public record Key(
            String kty,
            String kid,
            String use,
            String alg,
            String n,
            String e
    ) {}

    public Key getMatchedKey(String kid, String alg) {
        return keys.stream()
                .filter(key -> key.kid().equals(kid) && key.alg().equals(alg))
                .findFirst()
                .orElseThrow(() -> new AuthException(AuthErrorCode.INVALID_APPLE_TOKEN));
    }
}
