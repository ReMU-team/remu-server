package com.remu.domain.review.converter;

import com.remu.domain.galaxy.entity.Galaxy;
import com.remu.domain.resolution.entity.Resolution;
import com.remu.domain.review.dto.ReviewReqDTO;
import com.remu.domain.review.dto.ReviewResDTO;
import com.remu.domain.review.entity.Review;

import java.util.List;

public class ReviewConverter {

    /* ---------------------
     * [CREATE] 단일 생성 관련 변환
     * --------------------- */

    // Entity -> DTO
    public static ReviewResDTO.ReviewCreateDTO toCreateDTO(
            Review review
    ) {
        return ReviewResDTO.ReviewCreateDTO.builder()
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

    /* ---------------------
     * [CREATE] 배치 생성 관련 변환
     * --------------------- */

    // Entity -> DTO
    public static ReviewResDTO.ReviewBatchCreateDTO toBatchCreateResDTO(
            Galaxy galaxy,
            List<Review> reviews
    ) {
        List<ReviewResDTO.SingleReviewDTO> reviewDTOList = reviews.stream()
                .map(item -> ReviewResDTO.SingleReviewDTO.builder()
                        .resolutionId(item.getResolution().getId())
                        .resolutionContent(item.getResolution().getContent())
                        .reviewId(item.getId())
                        .reviewContent(item.getContent())
                        .isResolutionFulfilled(item.getIsResolutionFulfilled())
                        .createdAt(item.getCreatedAt())
                        .build())
                .toList();

        return ReviewResDTO.ReviewBatchCreateDTO.builder()
                .reviewEmojiId(galaxy.getReviewEmojiId())
                .resolutionEmojiId(galaxy.getResolutionEmojiId())
                .reflection(galaxy.getReflection())
                .reviews(reviewDTOList)
                .build();
    }


    // DTO -> Entity
    public static Review toReviewFromBatch(
            Resolution resolution,
            ReviewReqDTO.ReviewItemDTO dto
    ) {
        return Review.builder()
                .resolution(resolution)
                .content(dto.reviewContent())
                .isResolutionFulfilled(dto.isResolutionFulfilled())
                .build();
    }


    /* ---------------------
     * [READ] 조회 관련 변환
     * --------------------- */

    public static ReviewResDTO.ReviewPreviewListDTO toReviewPreviewListDTO(
            List<Review> reviews
    ) {
        return ReviewResDTO.ReviewPreviewListDTO.builder()
                .reviewList(reviews.stream()
                        .map(ReviewConverter::toReviewPreviewDTO)
                        .toList()
                )
                .listSize(reviews.size())
                .build();
    }

    public static ReviewResDTO.ReviewPreviewDTO toReviewPreviewDTO(
            Review review
    ) {
        return ReviewResDTO.ReviewPreviewDTO.builder()
                .reviewId(review.getId())
                .resolutionId(review.getResolution().getId())
                .resolutionContent(review.getResolution().getContent())
                .reviewContent(review.getContent())
                .isResolutionFulfilled(review.getIsResolutionFulfilled())
                .createdAt(review.getCreatedAt())
                .build();
    }

    /* ---------------------
     * [UPDATE] 단일 수정 관련 변환
     * --------------------- */

    public static ReviewResDTO.UpdateDTO toUpdateDTO(Review review) {
        return ReviewResDTO.UpdateDTO.builder()
                .reviewId(review.getId())
                .resolutionId(review.getResolution().getId())
                .resolutionContent(review.getResolution().getContent())
                .reviewContent(review.getContent())
                .isResolutionFulfilled(review.getIsResolutionFulfilled())
                .updatedAt(review.getUpdatedAt())
                .build();
    }

    /* ---------------------
     * [UPDATE] 배치 수정 관련 변환
     * --------------------- */

    // Entity -> DTO
    public static ReviewResDTO.ReviewBatchUpdateDTO toBatchUpdateResDTO(
            Galaxy galaxy,
            List<Review> reviews
    ) {
        List<ReviewResDTO.SingleReviewUpdateDTO> reviewUpdateDTOList = reviews.stream()
                .map(item -> ReviewResDTO.SingleReviewUpdateDTO.builder()
                        .resolutionId(item.getResolution().getId())
                        .resolutionContent(item.getResolution().getContent())
                        .reviewId(item.getId())
                        .reviewContent(item.getContent())
                        .isResolutionFulfilled(item.getIsResolutionFulfilled())
                        .updatedAt(item.getUpdatedAt())
                        .build())
                .toList();

        return ReviewResDTO.ReviewBatchUpdateDTO.builder()
                .reviewEmojiId(galaxy.getReviewEmojiId())
                .resolutionEmojiId(galaxy.getResolutionEmojiId())
                .reflection(galaxy.getReflection())
                .reviews(reviewUpdateDTOList)
                .build();
    }
}
