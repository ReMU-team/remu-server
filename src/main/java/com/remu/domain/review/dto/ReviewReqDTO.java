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
            List<@Valid ResolutionReviewItemDTO> reviews
    ) {}

    public record ResolutionReviewItemDTO(
            @NotNull(message = "다짐 ID는 필수입니다.")
            Long resolutionId,

            @NotBlank(message = "회고 내용은 필수입니다.")
            String reviewContent,

            @NotNull(message = "다짐 성취 여부를 선택해주세요")
            Boolean isResolutionFulfilled

    ) {}

    public record UpdateDTO(

            @NotBlank(message = "회고 내용은 필수입니다.")
            String content,

            @NotNull(message = "다짐 성취 여부를 선택해주세요.")
            Boolean isResolutionFulfilled
    ) {}
}
