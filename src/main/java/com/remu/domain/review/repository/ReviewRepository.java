package com.remu.domain.review.repository;

import com.remu.domain.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    boolean existsByResolutionId(Long resolutionId);

    @Query("select r from Review r " +
            "join fetch r.resolution res " +
            "where res.galaxy.id = :galaxyId")
    List<Review> findAllByGalaxyId(@Param("galaxyId") Long galaxyId);
}
