package com.remu.domain.review.service;

import com.remu.domain.review.dto.ReviewReqDTO;
import com.remu.domain.review.dto.ReviewResDTO;

public interface ReviewService {

    // 회고 배치 생성
    ReviewResDTO.ReviewBatchCreateDTO batchCreate(
            Long userId,
            Long galaxyId,
            ReviewReqDTO.BatchReviewCreateDTO dto
    );

    // 리뷰 배치 업데이트
    ReviewResDTO.ReviewBatchUpdateDTO batchUpdate(
            Long userId,
            Long galaxyId,
            ReviewReqDTO.BatchReviewUpdateDTO dto
    );

    // === Query 로직 (조회) ===
    ReviewResDTO.ReviewPreviewListDTO getReviewListByGalaxy(
            Long userId,
            Long galaxyId
    );
}
