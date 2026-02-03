package com.remu.domain.galaxy.repository;

import com.remu.domain.galaxy.entity.Galaxy;

import java.util.Optional;

public interface GalaxyQueryDsl {

    Optional<Galaxy> findByIdWithAllDetails(Long galaxyId);
}
