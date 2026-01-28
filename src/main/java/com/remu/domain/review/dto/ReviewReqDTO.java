package com.remu.domain.review.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class ReviewReqDTO {

    // 회고 단일 생성
    public record CreateDTO(

            @NotBlank(message = "회고 내용은 필수입니다.")
            String content,

            @NotNull(message = "다짐 성취 여부를 선택해주세요")
            Boolean isResolutionFulfilled
    ) {}

    // 회고 배치 생성
    public record BatchReviewCreateDTO(
            @NotNull(message = "회고 이모지는 필수입니다.")
            String emojiId,

            @NotBlank(message = "여행 후기는 필수입니다.")
            String reflection,

            @NotEmpty(message = "각 다짐에 대한 회고 리스트가 비어있습니다.")
            List<@Valid ReviewItemDTO> reviews
    ) {}

    public record ReviewItemDTO(
            @NotNull(message = "다짐 ID는 필수입니다.")
            Long resolutionId,

            @NotBlank(message = "회고 내용은 필수입니다.")
            String reviewContent,

            @NotNull(message = "다짐 성취 여부를 선택해주세요")
            Boolean isResolutionFulfilled

    ) {}

    // 회고 단일 수정

    public record UpdateDTO(

            @NotBlank(message = "회고 내용은 필수입니다.")
            String content,

            @NotNull(message = "다짐 성취 여부를 선택해주세요.")
            Boolean isResolutionFulfilled
    ) {}

    // 회고 배치 수정
    public record BatchReviewUpdateDTO(
            @NotBlank(message = "회고 이모지는 필수입니다.")
            String emojiId,

            @NotBlank(message = "여행 후기는 필수입니다.")
            String reflection,

            @NotEmpty(message = "수정할 회고 리스트가 비어있습니다.")
            List<@Valid ReviewUpdateItemDTO> reviews
    ) {}

    public record ReviewUpdateItemDTO(
            @NotNull(message = "회고 ID는 필수입니다")
            Long reviewId,

            @NotBlank(message = "수정할 회고 내용은 필수입니다.")
            String reviewContent,

            @NotNull(message = "성취 여부를 선택해주세요")
            Boolean isResolutionFulfilled
    ) {}
}
