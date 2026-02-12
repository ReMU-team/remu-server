package com.remu.domain.user.repository;

import com.remu.domain.user.entity.User;
import com.remu.domain.user.enums.SocialType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByName(String name);

    Optional<User> findBySocialTypeAndSocialId(SocialType socialType, String socialId);

    boolean existsByNameAndIdNot(String name, Long id);
}