package com.remu.global.auth.repository;

import com.remu.global.auth.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByTokenHash(String tokenHash);

    void deleteByUserId(Long userId);
    void deleteByTokenHash(String tokenHash);

    boolean existsByTokenHash(String hash);
}
