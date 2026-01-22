package com.remu.domain.review.service;

import com.remu.domain.review.dto.ReviewReqDTO;
import com.remu.domain.review.dto.ReviewResDTO;

public interface ReviewService {
    ReviewResDTO.CreateDTO create(
            Long userId,
            Long resolutionId,
            ReviewReqDTO.CreateDTO dto
    );
}
