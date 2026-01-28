package com.remu.domain.review.dto;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

public class ReviewResDTO {

    // 단일 생성
    @Builder
    public record ReviewCreateDTO(
            Long resolutionId,
            Long reviewId,
            String content,
            Boolean isResolutionFulfilled,
            LocalDateTime createdAt
    ) {}

    // 배치 생성
    @Builder
    public record ReviewBatchCreateDTO(
            String reviewEmojiId,
            String resolutionEmojiId,
            String reflection,
            List<SingleReviewDTO> reviews

    ) {}

    @Builder
    public record SingleReviewDTO(
            Long resolutionId,
            String resolutionContent,
            Long reviewId,
            String reviewContent,
            Boolean isResolutionFulfilled,
            LocalDateTime createdAt
    ) {}

    // 다짐 조회

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

    // 회고 단일 수정

    @Builder
    public record UpdateDTO(
            Long reviewId,
            Long resolutionId,
            String resolutionContent,
            String reviewContent,
            Boolean isResolutionFulfilled,
            LocalDateTime updatedAt
    ) {}

    // 회고 배치 수정
    @Builder
    public record ReviewBatchUpdateDTO(
            String reviewEmojiId,
            String resolutionEmojiId,
            String reflection,
            List<SingleReviewUpdateDTO> reviews
    ) {}

    @Builder
    public record SingleReviewUpdateDTO(
            Long resolutionId,
            String resolutionContent,
            Long reviewId,
            String reviewContent,
            Boolean isResolutionFulfilled,
            LocalDateTime updatedAt
    ) {}
}
