package com.remu.domain.review.service;

import com.remu.domain.review.dto.ReviewReqDTO;
import com.remu.domain.review.dto.ReviewResDTO;

public interface ReviewService {
    ReviewResDTO.ReviewCreateDTO create(
            Long userId,
            Long resolutionId,
            ReviewReqDTO.CreateDTO dto
    );

    // 회고 배치 생성
    ReviewResDTO.ReviewBatchCreateDTO batchCreate(
            Long userId,
            Long galaxyId,
            ReviewReqDTO.BatchReviewCreateDTO dto
    );

    ReviewResDTO.UpdateDTO update(
            Long userId,
            Long reviewId,
            ReviewReqDTO.UpdateDTO dto
    );

    // === Query 로직 (조회) ===
    ReviewResDTO.ReviewPreviewListDTO getReviewListByGalaxy(
            Long userId,
            Long galaxyId
    );
}
