package com.remu.domain.galaxy.repository;

import com.remu.domain.galaxy.entity.Galaxy;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GalaxyRepository extends JpaRepository<Galaxy, Long>, GalaxyQueryDsl {
    Slice <Galaxy> findAllByUserId(Long userId, Pageable pageable);

    // 총 개수
    Long countByUserId(Long userId);
}
