package com.remu.domain.review.dto;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

public class ReviewResDTO {

    @Builder
    public record CreateDTO(
            Long resolutionId,
            Long reviewId,
            String content,
            Boolean isResolutionFulfilled,
            LocalDateTime createdAt
    ) {}

    @Builder
    public record ReviewPreviewListDTO(
            List<ReviewPreviewDTO> reviewList,
            Integer listSize
    ) {}

    @Builder
    public record ReviewPreviewDTO(
            Long reviewId,
            Long resolutionId,
            String resolutionContent, // 어떤 다짐에 대한 회고인지 명시
            String reviewContent,
            Boolean isResolutionFulfilled,
            LocalDateTime createdAt
    ) {}

    @Builder
    public record UpdateDTO(
            Long reviewId,
            Long resolutionId,
            String resolutionContent,
            String reviewContent,
            Boolean isResolutionFulfilled,
            LocalDateTime updatedAt
    ) {}
}
