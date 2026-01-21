package com.remu.domain.user.repository;

import com.remu.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByName(String name);
}