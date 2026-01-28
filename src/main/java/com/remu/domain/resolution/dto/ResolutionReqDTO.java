package com.remu.domain.resolution.dto;

import com.remu.domain.galaxy.entity.Galaxy;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.List;

// Req: 유저가 시스템에 요청

public class ResolutionReqDTO {

    // 다짐 생성 단일 요청 DTO
    public record CreateDTO(
            @NotBlank(message = "다짐 내용은 필수입니다.")
            @Size(max = 100, message = "다짐은 최대 100자까지 가능합니다.")
            String content,

            @NotBlank(message = "다짐 이모지 정보는 필수입니다.")
            String emojiId
    ) {}

    // 다짐 생성 배치 요청 DTO
    public record BatchCreateDTO(
            @NotBlank(message = "이모지 선택은 필수입니다.")
            String emojiId,

            @NotEmpty(message = "최소 하나의 다짐은 필수입니다.")
            List<@NotBlank(message = "다짐 내용은 비어있을 수 없습니다.")
                @Size(max = 100, message ="다짐은 최대 100자까지 가능합니다.")
                    String> contents
    ) {}

    // 다짐 수정 DTO
    public record UpdateDTO(
            String content
    ) {}
}
