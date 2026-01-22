package com.remu.domain.review.service;

import com.remu.domain.review.dto.ReviewReqDTO;
import com.remu.domain.review.dto.ReviewResDTO;
import org.springframework.transaction.annotation.Transactional;

public interface ReviewService {
    ReviewResDTO.CreateDTO create(
            Long userId,
            Long resolutionId,
            ReviewReqDTO.CreateDTO dto
    );

    // === Query 로직 (조회) ===
    ReviewResDTO.ReviewPreviewListDTO getReviewListByGalaxy(
            Long userId,
            Long galaxyId
    );
}
