package com.remu.domain.galaxy.repository;

import com.remu.domain.galaxy.entity.Galaxy;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GalaxyRepository extends JpaRepository<Galaxy, Long> {
}
