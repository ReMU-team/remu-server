package com.remu.domain.review.converter;

import com.remu.domain.resolution.entity.Resolution;
import com.remu.domain.review.dto.ReviewReqDTO;
import com.remu.domain.review.dto.ReviewResDTO;
import com.remu.domain.review.entity.Review;

public class ReviewConverter {

    /* ---------------------
     * [CREATE] 생성 관련 변환
     * --------------------- */

    // Entity -> DTO
    public static ReviewResDTO.CreateDTO toCreateDTO(
            Review review
    ) {
        return ReviewResDTO.CreateDTO.builder()
                .reviewId(review.getId())
                .resolutionId(review.getResolution().getId())
                .content(review.getContent())
                .isResolutionFulfilled(review.getIsResolutionFulfilled())
                .createdAt(review.getCreatedAt())
                .build();
    }

    // DTO -> Entity
    public static Review toReview(
            ReviewReqDTO.CreateDTO dto, Resolution resolution
    ) {
        return Review.builder()
                .resolution(resolution)
                .content(dto.content())
                .isResolutionFulfilled(dto.isResolutionFulfilled())
                .build();
    }
}
