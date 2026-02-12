package com.remu.domain.feedback.repository;

import com.remu.domain.feedback.entity.AiFeedback;
import com.remu.domain.galaxy.entity.Galaxy;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AiFeedbackRepository extends JpaRepository<AiFeedback, Long> {
    boolean existsByGalaxy(Galaxy galaxy);

    Optional<AiFeedback> findByGalaxyId(Long galaxyId);
}
