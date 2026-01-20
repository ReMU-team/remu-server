package com.remu.domain.resolution.repository;

import com.remu.domain.galaxy.entity.Galaxy;
import com.remu.domain.resolution.entity.Resolution;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ResolutionRepository extends JpaRepository<Resolution, Long> {

    List<Resolution> findAllByGalaxyId(Long galaxyId);
}
