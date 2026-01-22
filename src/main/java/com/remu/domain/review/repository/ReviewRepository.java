package com.remu.domain.review.repository;

import com.remu.domain.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    boolean existsByResolutionId(Long resolutionId);
}
