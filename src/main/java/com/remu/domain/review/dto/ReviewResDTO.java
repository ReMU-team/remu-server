package com.remu.domain.review.dto;

import lombok.Builder;

import java.time.LocalDateTime;

public class ReviewResDTO {

    @Builder
    public record CreateDTO(
            Long resolutionId,
            Long reviewId,
            String content,
            Boolean isResolutionFulfilled,
            LocalDateTime createdAt
    ) {}
}
