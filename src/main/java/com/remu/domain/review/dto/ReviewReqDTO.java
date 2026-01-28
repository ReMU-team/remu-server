package com.remu.domain.review.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ReviewReqDTO {

    // 회고 단일 생성
    public record CreateDTO(

            @NotBlank(message = "회고 내용은 필수입니다.")
            String content,

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
