package com.remu.domain.resolution.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

// Req: 유저가 시스템에 요청

public class ResolutionReqDTO {

    // 다짐 생성 배치 요청 DTO
    public record ResolutionBatchCreateDTO(
            @NotBlank(message = "이모지 선택은 필수입니다.")
            String emojiId,

            @NotBlank(message = "일러스트 정보는 필수입니다.")
            String illustId,

            @NotEmpty(message = "최소 하나의 다짐은 필수입니다.")
            List<@NotBlank(message = "다짐 내용은 비어있을 수 없습니다.")
                @Size(max = 100, message ="다짐은 최대 100자까지 가능합니다.")
                    String> contents
    ) {}

    // 다짐 수정 배치 DTO
    public record ResolutionBatchUpdateDTO(
            @NotBlank(message = "이모지 선택은 필수입니다.")
            String emojiId,

            @NotEmpty(message = "수정할 다짐 리스트가 비어있습니다.")
            List<@Valid ResolutionUpdateItemDTO> resolutions
    ) {}

    public record ResolutionUpdateItemDTO(
            @NotNull(message = "다짐 ID는 필수입니다.")
            Long resolutionId,

            @NotBlank(message = "내용은 비어있을 수 없습니다.")
            @Size(max = 100, message = "내용은 100자 이내여야 합니다.")
            String content
    ) {}
}
